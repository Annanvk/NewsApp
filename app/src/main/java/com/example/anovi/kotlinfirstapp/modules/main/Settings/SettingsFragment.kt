package com.example.anovi.kotlinfirstapp.modules.main.Settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Spinner
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.layout.fragment_settings
import com.example.anovi.kotlinfirstapp.common.*
import com.example.anovi.kotlinfirstapp.modules.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    var adapterCountry: SpinnerCountryAdapter? = null
    var adapterLanguage: SpinnerLanguageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterCountry = SpinnerCountryAdapter(context!!)
        adapterLanguage = SpinnerLanguageAdapter(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(fragment_settings, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity<AppCompatActivity> {
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.settings)
            it.text_source.invisible()
        }
        spinner_country.adapter = adapterCountry
        spinner_language.adapter = adapterLanguage
    }

    override fun onStart() {
        super.onStart()
        val sp: SharedPreferences by lazy {
            context!!.getSharedPreferences(SHARED_PREFERENCES_FILENAME,
                    Context.MODE_PRIVATE)
        }
        val editor = sp.edit()
        spinner_country.setSelection(adapterCountry!!.listCountry.indexOf(sp.getString(SHARED_PREFERENCES_COUNTRY, "")))
        fun setSpinner(spin: Spinner, adapter: BaseAdapter, savingKey: String) {
            spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val savingValue = adapter.getItem(position) as String
                    editor.putString(savingKey, savingValue)
                    editor.apply()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

        setSpinner(spinner_country, adapterCountry!!, SHARED_PREFERENCES_COUNTRY)
        setSpinner(spinner_language, adapterLanguage!!, SHARED_PREFERENCES_LANGUAGE)
    }
}