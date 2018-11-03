package com.example.anovi.kotlinfirstapp.modules.main.NewsDetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.anovi.kotlinfirstapp.R.layout.fragment_web_view
import com.example.anovi.kotlinfirstapp.common.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_web_view.*

class NewsDetailFragment : Fragment() {

    var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        news = arguments!!.getParcelable(NEWS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(fragment_web_view, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity<AppCompatActivity> {
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar!!.setDisplayShowTitleEnabled(true)
            it.title = news!!.title
        }
    }

    override fun onStart() {
        super.onStart()
        activity!!.progress_bar.visible()
        web_view.loadUrl(news?.url)
        web_view.webViewClient = WebViewClient()
        activity!!.progress_bar.invisible()
    }
}


