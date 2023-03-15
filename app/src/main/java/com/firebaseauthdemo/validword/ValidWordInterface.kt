package com.firebaseauthdemo.validword

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ValidWordInterface {

    @GET("dictionary")
    @Headers("X-Api-Key: tySaRZBIbSKCkfr3xiesyLoAQvdFCH0ivKQrhC7Z")
    suspend fun validWord(@Query("word") word: String): Response<ValidWordData>
}