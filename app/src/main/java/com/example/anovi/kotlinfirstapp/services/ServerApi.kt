package com.example.anovi.kotlinfirstapp.services

import com.example.anovi.kotlinfirstapp.common.News
import com.example.anovi.kotlinfirstapp.common.Source
import io.reactivex.Observable

interface ServerApi {
    fun getNewsSources(nameLanguage: String, nameCountry: String): Observable<Array<Source>>
    fun getNews(newsSource: String, q: String, id: String, pageSize: Int, page: Int): Observable<Array<News>>
}