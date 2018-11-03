package com.example.anovi.kotlinfirstapp.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(val id: String, val name: String, val description: String, val country: String): Parcelable
@Parcelize
data class News(val title: String, val description: String, val url: String,  val urlToImage: String): Parcelable

