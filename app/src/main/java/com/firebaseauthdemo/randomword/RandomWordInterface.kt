package com.firebaseauthdemo.randomword

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface RandomWordInterface {

    @GET("randomword")
    @Headers("X-Api-Key: tySaRZBIbSKCkfr3xiesyLoAQvdFCH0ivKQrhC7Z")
    suspend fun getWord(): Response<RandomWordData>
}