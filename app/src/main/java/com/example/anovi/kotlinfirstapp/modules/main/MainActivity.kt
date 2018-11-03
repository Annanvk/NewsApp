package com.example.anovi.kotlinfirstapp.modules.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.anovi.kotlinfirstapp.R
import com.example.anovi.kotlinfirstapp.common.*
import com.example.anovi.kotlinfirstapp.modules.main.NewsDetail.NewsDetailFragment
import com.example.anovi.kotlinfirstapp.modules.main.PagerNews.PageFragment
import com.example.anovi.kotlinfirstapp.modules.main.Settings.SettingsFragment
import com.example.anovi.kotlinfirstapp.modules.main.Source.SourceFragment
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity() {

    var drawerResult: Drawer? = null
    var navController : NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.container)
        initDrawer()
    }

    private fun initDrawer() {
        val drawerBuilder = DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.header)
                .withDrawerGravity(Gravity.RIGHT)

        drawerItems.forEach {
            drawerBuilder.addDrawerItems(it)
        }

        drawerBuilder.withOnDrawerItemClickListener { _, position, _ ->
            saveValueForCategory(position)
            progress_bar.visible()
            val intent = Intent(ACTION_NAVIGATION_ITEM_CLICK)
            LocalBroadcastManager.getInstance(applicationContext!!).sendBroadcast(intent)
            drawerResult!!.closeDrawer()
            true
        }
        drawerResult = drawerBuilder.build()
        drawerResult!!.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

   private fun saveValueForCategory(position: Int) {
        val sp by lazy { getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE) }
        val editor = sp.edit()
        val q = Category.values()[position - 1].v
        editor.putString(EDITOR_VALUE, q)
        editor.apply()
    }

   private var drawerItems: Array<SecondaryDrawerItem> = arrayOf()
        get() {
            var arr: Array<SecondaryDrawerItem> = arrayOf()
            for (c in Category.values()) {
                val item = SecondaryDrawerItem().withName(c.n)
                arr += item
            }
            return arr
        }

    override fun onBackPressed() {
        when (drawerResult!!.isDrawerOpen) {
            true -> drawerResult!!.closeDrawer()
            else -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}