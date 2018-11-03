package com.example.anovi.kotlinfirstapp.modules.main.Source

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.example.anovi.kotlinfirstapp.R.layout.list_item
import com.example.anovi.kotlinfirstapp.common.Source
import com.example.anovi.kotlinfirstapp.common.log_i
import kotlinx.android.synthetic.main.list_item.view.*

@Suppress("CAST_NEVER_SUCCEEDS")
class SourceAdapter(var mItems: Array<Source>) : RecyclerView.Adapter<SourceAdapter.ViewHolder>(), Filterable {

    private var filteredList: ArrayList<Source>? = null
    lateinit var onClickItem: (s: Source) -> Unit

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView = view.cardView!!
        var name = view.text_name!!
        var description = view.text_description!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(list_item, parent, false))

    override fun getItemCount(): Int = filteredList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = filteredList!![position]
        with(holder) {
            name.text = news.name
            description.text = news.description
            cardView.setOnClickListener {
                onClickItem(news)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                val results = Filter.FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filteredList = ArrayList(mItems.size)
                    filteredList!!.addAll(mItems)
                    results.values = mItems
                    results.count = mItems.size
                } else {
                    val f = mItems.filter { it.name.toLowerCase().contains(constraint) }
                    filteredList = ArrayList(f.size)
                    log_i(f.size.toString())
                    filteredList!!.addAll(f)
                    results.values = filteredList
                    results.count = filteredList?.size ?: 0
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                notifyDataSetChanged()
            }
        }
    }
}