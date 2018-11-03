package com.example.anovi.kotlinfirstapp.services

import com.example.anovi.kotlinfirstapp.services.serverApi.ServerApiImpl

object Services {
      val serverApi: ServerApi by lazy { ServerApiImpl() }
}