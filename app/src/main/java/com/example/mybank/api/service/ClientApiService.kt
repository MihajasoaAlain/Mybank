package com.example.mybank.api.service

import ClientBancaire
import com.example.mybank.api.data.dto.StatsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClientApiService {
    @GET("client")
    suspend fun getClients(): List<ClientBancaire>

    @POST("client")
    suspend fun addClient(@Body client: ClientBancaire)

    @PATCH("client/{id}")
    suspend fun updateClient(@Path("id") id: String, @Body client: ClientBancaire)

    @DELETE("client/{id}")
    suspend fun deleteClient(@Path("id") id: String)

    @GET("client/stats")
    suspend fun getStats(): StatsResponse
}
