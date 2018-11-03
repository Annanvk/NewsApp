package com.example.anovi.kotlinfirstapp.modules.main.Settings

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.layout.spinner_country
import kotlinx.android.synthetic.main.spinner_country.view.*
import java.util.*

class SpinnerCountryAdapter() : BaseAdapter() {

    var listCountry = arrayListOf<String>()
    lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context

        listCountry.add("")
        Locale.getISOCountries().forEach { listCountry.add(it) }
        Collections.sort(listCountry, (compareBy { getCountryName(it) }))
    }

    private fun getCountryName(countryCode: String) = Locale("", countryCode).displayCountry

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater by lazy { context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater }
        val view: View = inflater.inflate(spinner_country, parent, false)
        when (position) {
            0 -> view.country.text = context.getString(R.string.all)
            else -> view.country.text = getCountryName(getItem(position))
        }
        return view
    }

    override fun getItem(position: Int): String = listCountry[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = listCountry.size
}