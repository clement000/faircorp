package com.faircorp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RoomApiService {
    @GET("room")
    fun findAll(): Call<List<RoomDto>>

    @GET("room/{id}")
    fun findById(@Path("id") id: Long): Call<RoomDto>
}