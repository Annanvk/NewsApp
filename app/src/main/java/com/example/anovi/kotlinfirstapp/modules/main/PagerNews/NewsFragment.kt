package com.example.anovi.kotlinfirstapp.modules.main.PagerNews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.layout.news_pager
import com.example.anovi.kotlinfirstapp.common.*
import com.example.anovi.kotlinfirstapp.modules.main.MainActivity
import com.example.anovi.kotlinfirstapp.services.server
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.news_pager.*

class NewsFragment : Fragment() {

    var loading = false
    var spinnerAdapter: SpinnerAdapter? = null
    var source: Source? = null
    var newsType: String? = null
    var newsAdapter: NewsAdapter? = null
    private val pageSize = 20
    private var page: Int? = null
    var q: String? = null
    private var request: Disposable? = null
    val sp by lazy { context!!.getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE) }

    private val br = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action) {
                ACTION_NAVIGATION_ITEM_CLICK -> {
                    q = sp.getString(EDITOR_VALUE, "")
                    newsAdapter!!.mNews = arrayOf()
                    loadData(q!!, source!!, page!!)
                }
            }
        }
    }

    companion object {
        fun newInstance(source: Source, newsType: String): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putParcelable("source", source)
            args.putString("newsType", newsType)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        q = sp.getString(EDITOR_VALUE, "")
        source = arguments!!.getParcelable("source")
        newsType = arguments!!.getString("newsType")
        newsAdapter = NewsAdapter()
        newsAdapter!!.clickOpenNewsDetail = {
            val bundle = Bundle()
            bundle.putParcelable(NEWS, it)
            findNavController().navigate(R.id.action_newsFragment_to_newsDetailFragment, bundle)
        }
        spinnerAdapter = SpinnerAdapter(context!!, arrayOf())
        page = (newsAdapter!!.itemCount / pageSize) + 1
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(news_pager, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view2.layoutManager = LinearLayoutManager(context)
        recycler_view2.adapter = newsAdapter

        recycler_view2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val lastVisibleItems = (recycler_view2.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loading && (lastVisibleItems + 1) == newsAdapter!!.itemCount && lastVisibleItems > 18 && lastVisibleItems < 99) {
                    progress_bar_news.visible()
                    loading = true
                    page = (newsAdapter!!.itemCount / pageSize) + 1
                    q = sp.getString(EDITOR_VALUE, "")
                    loadData(q!!, source!!, page!!)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ACTION_NAVIGATION_ITEM_CLICK)
        LocalBroadcastManager.getInstance(activity!!.applicationContext).registerReceiver(br, filter)
    }

    override fun onPause() {
        super.onPause()
        stopLoadData()
        LocalBroadcastManager.getInstance(activity!!.applicationContext).unregisterReceiver(br)
    }

    private fun stopLoadData() {
        request?.dispose()
    }

    private fun loadData(q: String, source: Source, page: Int) {

        activity<MainActivity> { it.progress_bar.visible() }
        request = server.getNews(newsType!!, q, source.id, pageSize, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    log_i(it.toString())
                    newsAdapter!!.addNews(it)
                    loading = false
                    progress_bar_news.invisible()
                    activity<MainActivity> {
                        it.container.isVisible
                        it.progress_bar.invisible()
                        it.progress_for_news.invisible()
                        when (newsAdapter!!.mNews.isEmpty()) {
                            true -> it.text_news.visible()
                        }
                    }
                },
                        {
                            alert(getString(R.string.warning)) {
                                positiveButton(getString(R.string.try_again), { dialog ->
                                    loadData(q, source, page)
                                    dialog.dismiss()
                                })
                            }?.show()
                        })
    }

    fun setTab(_source: Source) {
        source = _source
        newsAdapter!!.mNews = arrayOf()
        newsAdapter!!.notifyDataSetChanged()
        loadData(q!!, _source, page!!)
    }
}