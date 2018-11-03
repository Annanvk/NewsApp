package com.example.anovi.kotlinfirstapp.common

import android.content.DialogInterface
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.anovi.kotlinfirstapp.BuildConfig
import com.example.anovi.kotlinfirstapp.modules.main.MainActivity
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

inline fun onlyDebugConsume(f: () -> Unit) {
    if (BuildConfig.DEBUG) {
        f()
    }
}
//Log
fun log_v(tag: String, msg: String) = onlyDebugConsume { Log.v(tag, msg) }

fun log_d(tag: String, msg: String) = onlyDebugConsume { Log.d(tag, msg) }
fun log_i(tag: String, msg: String) = onlyDebugConsume { Log.i(tag, msg) }
fun log_w(tag: String, msg: String) = onlyDebugConsume { Log.w(tag, msg) }
fun log_e(tag: String, msg: String) = onlyDebugConsume { Log.e(tag, msg) }
fun log_v(msg: String) = log_v("_", msg)
fun log_d(msg: String) = log_d("_", msg)
fun log_i(msg: String) = log_i("_", msg)
fun log_w(msg: String) = log_w("_", msg)
fun log_e(msg: String) = log_e("_", msg)
//Pattern.compile("/^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?\$/").matcher(this).matches()

inline fun android.support.v4.app.Fragment.alert(
        message: CharSequence,
        title: CharSequence? = null,
        noinline init: (AlertBuilder<DialogInterface>.() -> Unit)? = null
) = activity?.alert(message, title, init)

inline fun android.support.v4.app.Fragment.toast(message: CharSequence) = activity!!.toast(message)

fun View.visible(){
    visibility = View.VISIBLE
}
fun View.invisible(){
    visibility = View.INVISIBLE
}
fun View.gone(){
    visibility = View.GONE
}

fun <T: AppCompatActivity> Fragment.activity(f: (T)->Unit){
    if (activity != null){
        f(activity as T)
    }
}