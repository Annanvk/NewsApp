package com.example.anovi.kotlinfirstapp.services.serverApi

import com.example.anovi.kotlinfirstapp.common.News
import com.example.anovi.kotlinfirstapp.common.Source
import com.example.anovi.kotlinfirstapp.services.ServerApi
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.Exception

class ServerApiImpl : ServerApi {

    private val API_KEY: String = "c4e3d0115d2344538838a7fc63628c06"

    override fun getNewsSources(nameLanguage: String, nameCountry: String) = service.getNewsSources(nameLanguage, nameCountry, API_KEY)
                .map{
                    when {
                        it.sources != null -> it.sources
                        else -> throw ServerException(1003)
                    }
                }

    override fun getNews(newsSource: String, q: String, id: String, pageSize: Int, page: Int) = service.getNews(newsSource, q, id, pageSize, page, API_KEY)
            .map{
                when{
                    it.articles != null -> it.articles
                    else -> throw ServerException(1003)
                }
            }

    private val NEWS_URL = "https://newsapi.org"

    interface AppService {

        @GET("/v2/sources")
        fun getNewsSources(@Query("language") nameLanguage: String, @Query("country") nameCountry: String, @Query("apikey") apiKey: String): Observable<SourcesResponse>

        @GET("/v2/{newsSource}")
        fun getNews(@Path("newsSource") newsSource: String, @Query("q") q: String, @Query("sources") sources: String, @Query("pageSize") pageSize: Int, @Query("page") page: Int, @Query("apiKey") apiKey: String): Observable<NewsResponse>
    }

    private val service: AppService by lazy { create<AppService>(NEWS_URL) }
}

data class SourcesResponse(val status: String, val message: String?, val sources: Array<Source>?)
data class NewsResponse(val status: String, val totalResult: Int, val articles: Array<News>?)

data class ServerException(val code: Int?) : Exception()