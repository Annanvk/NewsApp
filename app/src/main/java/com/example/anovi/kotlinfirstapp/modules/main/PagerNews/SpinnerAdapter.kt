package com.example.anovi.kotlinfirstapp.modules.main.PagerNews

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.anovi.kotlinfirstapp.R.layout.spinner_item
import com.example.anovi.kotlinfirstapp.common.Source
import kotlinx.android.synthetic.main.spinner_item.view.*

class SpinnerAdapter() : BaseAdapter() {

    var context: Context? = null
    var list: Array<Source>? = null

    constructor(context: Context, list: Array<Source>) : this() {
        this.context = context
        this.list = list
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater by lazy {context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater}
        val view by lazy {inflater.inflate(spinner_item, parent, false)}
        view.text_view_spinner.text = getItem(position).name
        return view
    }

    override fun getItem(position: Int): Source = list!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list!!.size
}