package com.example.anovi.kotlinfirstapp.modules.main.Source

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.R.id.*
import com.example.anovi.kotlinfirstapp.R.layout.fragment_source
import com.example.anovi.kotlinfirstapp.common.*
import com.example.anovi.kotlinfirstapp.modules.main.MainActivity
import com.example.anovi.kotlinfirstapp.services.server
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_source.*

@Suppress("NAME_SHADOWING")
class SourceFragment : Fragment() {

    var sourceAdapter: SourceAdapter? = null
    private var request: Disposable? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sourceAdapter = SourceAdapter(arrayOf())
        sourceAdapter!!.onClickItem = {
            val bundle = Bundle()
            bundle.putParcelable(SOURCE, it)
            bundle.putParcelableArray(LIST, sourceAdapter!!.mItems)
            findNavController().navigate(R.id.action_sourceFragment_to_pageFragment2, bundle)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(fragment_source, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.adapter = sourceAdapter
        activity<AppCompatActivity> {
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar!!.setDisplayShowTitleEnabled(true)
            it.title = getString(R.string.kotlinFirstApp)

        }
    }

    private fun startLoadData(nameC: String, nameL: String) {
        loadData(nameL, nameC)
    }

    private fun stopLoadData() {
        activity<MainActivity> { it.progress_bar.invisible() }
        request?.dispose()
    }

    private fun loadData(nameC: String, nameL: String) {
        activity<MainActivity> { it.progress_bar.visible() }
        request = server.getNewsSources(nameL, nameC)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    sourceAdapter!!.mItems = it
                    log_i(it.toString())
                    activity<MainActivity> {
                        it.container.isVisible
                        it.progress_bar.invisible()
                        when (sourceAdapter!!.mItems.isEmpty()) {
                            true -> it.text_source.visible()
                        }
                    }
                    sourceAdapter!!.getFilter().filter("")
                },
                        {
                            log_i(it.message.toString())
                            alert(getString(R.string.warning)) {
                                positiveButton(R.string.try_again, { dialog ->
                                    startLoadData(nameL, nameC)
                                    dialog.dismiss()
                                })
                            }?.show()
                        })
    }

//    fun request(nameC: String) {
//        activity!!.progress_bar.visibility = View.VISIBLE
//        async(CommonPool) {
//            val result = server.getNewsSources(nameC)
//            log_i(result.toString())
//            when {
//                result.isSuccess -> {
//                    //adapter!!.filteredList!!.addAll(result.data!!)
//                    adapter!!.mItems = result.data!!
//                    async(UI) {
//                        activity!!.container.visibility = View.VISIBLE
//                        activity!!.progress_bar.visibility = View.INVISIBLE
//                        adapter!!.getFilter().filter("")
//                        // adapter!!.notifyDataSetChanged()
//                    }
//                }
//                else -> async(UI) {
//                    alert("Hi! It's problems with the Internet!") {
//                        positiveButton("try again") { request(nameC) }
//                    }?.show()
//                }
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        val sp: SharedPreferences by lazy { context!!.getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE) }
        val nameC = sp.getString(SHARED_PREFERENCES_COUNTRY, "")
        val nameL = sp.getString(SHARED_PREFERENCES_LANGUAGE, "")
        startLoadData(nameL, nameC)
    }

    override fun onPause() {
        super.onPause()
        stopLoadData()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search_settings, menu)
        val searchItem by lazy { menu.findItem(R.id.search) }
        val searchView by lazy { searchItem.actionView as SearchView }
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String): Boolean {
                sourceAdapter!!.getFilter().filter(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            settings -> {
                findNavController().navigate(R.id.settingsFragment)
                true
            }
            drawer -> {
                activity<MainActivity> { it.drawerResult!!.openDrawer() }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        val imm by lazy { activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager }
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}