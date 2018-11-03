package com.example.anovi.kotlinfirstapp.services.serverApi

import com.example.anovi.kotlinfirstapp.common.log_d
import com.example.anovi.kotlinfirstapp.common.onlyDebugConsume
import com.example.anovi.kotlinfirstapp.services.Error
import com.example.anovi.kotlinfirstapp.services.Result
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


inline fun <reified T> create(baseUrl: String): ServerApiImpl.AppService {
    val httpClientBuilder = UnsafeOkHttpClient.getUnsafeOkHttpClient()
    httpClientBuilder.readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
    onlyDebugConsume {
        val loggingInterceptor = HttpLoggingInterceptor({ l -> log_d("HTTP", l) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)
    }

    val httpClient = httpClientBuilder
            .build()
    val sourcesService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
    return sourcesService.create(ServerApiImpl.AppService::class.java)
}


suspend fun <T> Call<T>.executeAsync(): T = suspendCancellableCoroutine { continuation ->

    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable) {
            continuation.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            if (response.isSuccessful) {
                if (response.code() == 200)
                    continuation.resume(response.body()!!)
                else
                    continuation.resume("" as T)
            } else {
                continuation.resumeWithException(retrofit2.HttpException(response))
            }
        }
    })
    continuation.invokeOnCompletion {
        if (continuation.isCancelled) {
            try {
                cancel()
            } catch (e: Throwable) {
            }
        }
    }
}


suspend fun <T> Call<T>.executeAsyncAndHandleError(): Result<T> = try {
    Result(executeAsync(), null)
} catch (e: UnknownHostException) {
    Result(null, Error.NetworkError())
} catch (e: SocketTimeoutException) {
    Result(null, Error.NetworkError())
} catch (e: HttpException) {
    Result(null, Error.HttpError(e.code(), e.message()))
} catch (e: MalformedJsonException) {
    Result(null, Error.DataParsingError())
} catch (e: Throwable) {
    Result(null, Error.UnknownError())
}