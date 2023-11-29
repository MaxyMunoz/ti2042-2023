package com.example.myfirebaseexample.api

import com.example.myfirebaseexample.api.response.AnimeResponse
import com.example.myfirebaseexample.api.response.PostResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

class FirebaseApiAdapter {
    private var URL_BASE = "https://evalucion-firebase-default-rtdb.firebaseio.com/"
    private val firebaseApi = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getAnimes(): MutableMap<String,AnimeResponse>? {
        val call = firebaseApi.create(FirebaseApi::class.java).getAnimes().execute()
        val animes = call.body()
        return animes
    }

    fun getAnime(id: String): AnimeResponse? {
        val call = firebaseApi.create(FirebaseApi::class.java).getAnime(id).execute()
        val anime = call.body()
        return anime
    }

    fun setAnime(anime: AnimeResponse): PostResponse? {
        val call = firebaseApi.create(FirebaseApi::class.java).setAnime(anime).execute()
        val results = call.body()
        return results
    }
}