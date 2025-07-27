package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("plants/")
    suspend fun GetBiljke(
        @Query("token") apiKey: String =BuildConfig.API_KEY
    ): Response<GetBiljkeResponse>

    @GET("plants/{id}")
    suspend fun GetBiljkeID(
        @Path("id") biljkaId: Int,
        @Query("token") apiKey: String =BuildConfig.API_KEY
    ): Response<GetBiljkeResponse2>

    @GET("plants/")
    suspend fun GetLatBiljke(
        @Query("filter[scientific_name]") latNaziv:String,
        @Query("token") apiKey: String =BuildConfig.API_KEY
    ): Response<GetBiljkeResponse>

    @GET("plants/search")
    suspend fun GetFlowerColor(
        @Query("filter[flower_color]") boja:String,
        @Query("q") substring:String,
        @Query("token") apiKey: String =BuildConfig.API_KEY
    ): Response<GetBiljkeResponse>
}