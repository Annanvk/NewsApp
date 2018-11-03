package com.example.anovi.kotlinfirstapp.modules.main.PagerNews

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.Spinner
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.id.drawer_two
import com.example.anovi.kotlinfirstapp.R.id.spinner_title_source
import com.example.anovi.kotlinfirstapp.R.layout.fragment_news
import com.example.anovi.kotlinfirstapp.R.menu.menu_spinner
import com.example.anovi.kotlinfirstapp.common.*
import com.example.anovi.kotlinfirstapp.modules.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news.*

class PageFragment : Fragment() {

    var list: Array<Source>? = null
    private var spinnerAdapter: SpinnerAdapter? = null
    var source: Source? = null
    private var newsAdapter: NewsAdapter? = null
    var pageAdapter: PageAdapter? = null
    private var page: Int? = null
    private val pageSize = 20

    companion object {
        fun newInstance(list: Array<Source>, source: Source): PageFragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putParcelableArray(LIST, list)
            args.putParcelable(SOURCE, source)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        list = arguments!!.getParcelableArray(LIST) as Array<Source>?
        source = arguments!!.getParcelable(SOURCE)
        newsAdapter = NewsAdapter(arrayOf())
        spinnerAdapter = SpinnerAdapter(context!!, list!!)
        pageAdapter = PageAdapter(childFragmentManager)
        page = (newsAdapter!!.itemCount / pageSize) + 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(fragment_news, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = pageAdapter
        activity<AppCompatActivity> {
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar!!.setDisplayShowTitleEnabled(false)
            it.text_source.invisible()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(menu_spinner, menu)
        val spinnerItem by lazy { menu.findItem(spinner_title_source) }
        val spinner: Spinner by lazy { spinnerItem.actionView as Spinner }
        spinner.adapter = spinnerAdapter
        spinner.setSelection(list!!.indexOf(source))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                source = list!![position]
                pageAdapter!!.updateWithNewsSource(source!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            drawer_two -> {
                activity<MainActivity> { it.drawerResult!!.openDrawer() }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private var tab1: NewsFragment? = null
        private var tab2: NewsFragment? = null
        private val titles = arrayOf(getString(R.string.everything), getString(R.string.top_headlines))

        override fun getItem(position: Int): Fragment =
                when (position) {
                    0 -> {
                        tab1 = NewsFragment.newInstance(source!!, getString(R.string.every_thing))
                        tab1!!
                    }
                    else -> {
                        tab2 = NewsFragment.newInstance(source!!, getString(R.string.top__headlines))
                        tab2!!
                    }
                }

        fun updateWithNewsSource(source: Source) {
            tab1!!.setTab(source)
            tab2!!.setTab(source)
        }

        override fun getCount() = titles.size

        override fun getPageTitle(position: Int): CharSequence = titles[position]
    }
}