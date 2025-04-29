package com.example.mybank.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.mybank.api.service.ClientApiService

object RetrofitInstance {
    // Ajouter le slash à la fin de l'URL
    private const val BASE_URL = "http://192.168.43.81:3000/"

    val api: ClientApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientApiService::class.java)
    }
}