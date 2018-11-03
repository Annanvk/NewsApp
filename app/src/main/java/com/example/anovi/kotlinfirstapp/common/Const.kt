package com.example.anovi.kotlinfirstapp.common

import com.example.anovi.kotlinfirstapp.R

enum class Category(var n: Int, var v: String){
    ALL(R.string.all, ""),
    TECHNOLOGY(R.string.technology, "technology"),
    MONEY(R.string.money, "money"),
    SPORT(R.string.sport, "sport"),
    EAT(R.string.eat, "eat"),
    CITY(R.string.city, "city"),
    FASHION(R.string.fashion, "fashion"),
    TRAVELING(R.string.traveling, "traveling"),
    HEALTH(R.string.health, "health")
}


const val ACTION_NAVIGATION_ITEM_CLICK = "ACTION_NAVIGATION_ITEM_CLICK"
const val SHARED_PREFERENCES_FILENAME = "SHARED_PREFERENCES_FILENAME"
const val EDITOR_VALUE = "EDITOR_VALUE"
const val SHARED_PREFERENCES_COUNTRY = "SHARED_PREFERENCES_COUNTRY"
const val SHARED_PREFERENCES_LANGUAGE = "SHARED_PREFERENCES_LANGUAGE"
const val NEWS = "NEWS"
const val LIST = "LIST"
const val SOURCE = "SOURCE"