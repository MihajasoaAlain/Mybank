package com.example.mybank.api.service

import ClientBancaire
import com.example.mybank.api.data.dto.StatsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClientApiService {
    @GET("client")
    suspend fun getClients(): List<ClientBancaire>

    @POST("client")
    suspend fun ajouterClient(@Body client: ClientBancaire)

    @PUT("client/{id}")
    suspend fun modifierClient(@Path("id") id: Int, @Body client: ClientBancaire)

    @DELETE("client/{id}")
    suspend fun supprimerClient(@Path("id") id: Int)

    @GET("client/stats")
    suspend fun getStats(): StatsResponse
}
