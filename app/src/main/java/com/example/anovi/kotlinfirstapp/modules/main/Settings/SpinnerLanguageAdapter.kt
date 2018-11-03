package com.example.anovi.kotlinfirstapp.modules.main.Settings

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.layout.spinner_language
import kotlinx.android.synthetic.main.spinner_language.view.*
import java.util.*

class SpinnerLanguageAdapter() : BaseAdapter() {

    var listLanguage = arrayListOf<String>()
    lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context

        listLanguage.add("")
        Locale.getISOLanguages().forEach { listLanguage.add(it) }
        Collections.sort(listLanguage, (compareBy { getLanguage(it) }))
    }

    private fun getLanguage(languageCode: String) = Locale(languageCode).displayLanguage

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }
        val view: View = inflater.inflate(spinner_language, parent, false)
        when (position) {
            0 -> view.language.text = context.getString(R.string.all)
            else -> view.language.text = getLanguage(getItem(position))
        }
        return view
    }

    override fun getItem(position: Int) = listLanguage[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = listLanguage.size
}