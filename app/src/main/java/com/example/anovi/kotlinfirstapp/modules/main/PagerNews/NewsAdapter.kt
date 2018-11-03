package com.example.anovi.kotlinfirstapp.modules.main.PagerNews

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.layout.news
import com.example.anovi.kotlinfirstapp.common.News
import com.example.anovi.kotlinfirstapp.common.invisible
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news.view.*
import org.jetbrains.anko.bundleOf
import java.lang.Exception

class NewsAdapter(var mNews: Array<News> = arrayOf()) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    fun addNews(newPage: Array<News>) {
        mNews += newPage
        notifyDataSetChanged()
    }

    lateinit var clickOpenNewsDetail: (n: News) -> Unit

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var cardView2 = v.cardView2
        var progress = v.progress_bar2
        var imageView = v.imageView
        var title = v.textView1
        var description = v.textView2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(news, parent, false))

    override fun getItemCount(): Int = mNews.size

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = mNews[position]
        with(holder) {
            title.text = news.title
            description.text = news.description
            Picasso.get()
                    .load(news.urlToImage)
                    .error(R.drawable.ic_error_black_24dp)
                    .resize(100, 100)
                    .centerCrop()
                    .into(imageView, object : Callback {
                        override fun onSuccess() {
                            progress.invisible()
                        }

                        override fun onError(e: Exception?) {
                            progress.invisible()
                        }
                    })
            cardView2.setOnClickListener {
                openNewDetail(news)
            }
        }
    }

    private fun openNewDetail(news: News){
        clickOpenNewsDetail(news)
    }
}