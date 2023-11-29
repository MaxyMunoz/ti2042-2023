package com.example.myfirebaseexample.api

import com.example.myfirebaseexample.api.response.PostResponse
import com.example.myfirebaseexample.api.response.AnimeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FirebaseApi {
    @GET("animes.json")
    fun getAnimes(): Call<MutableMap<String, AnimeResponse>>

    @GET("animes/{id}.json")
    fun getAnime(
        @Path("id") id: String
    ): Call<AnimeResponse>

    @POST("animes.json")
    fun setAnime(
        @Body() body: AnimeResponse
    ): Call<PostResponse>
}