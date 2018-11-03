package com.example.anovi.kotlinfirstapp.services

val server: ServerApi = Services.serverApi
//    get() {
//        return Services.serverApi
//    }
data class Result<T>(val data: T? = null, val error: Error? = null) {
    val isSuccess: Boolean get() = data != null
}
sealed class Error {
    data class UnknownError(val code: Int = 1000, val msg: String = "Unknown error") : Error()
    data class NetworkError(val code: Int = 1001, val msg: String = "Network error") : Error()
    data class DataParsingError(val code: Int = 1002, val msg: String = "Data parsing error") : Error()
    data class ServerError(val code: Int = 1003, val msg: String = "Server error") : Error()
    data class HttpError(val code: Int, val msg: String) : Error()
}
