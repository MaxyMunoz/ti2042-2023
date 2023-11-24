package com.example.myfirebaseexample.api.response

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("name") var name: String,
    @SerializedName("cap") var cap: Int,
    @SerializedName("link") var link: String,
    @SerializedName("img") var img:String
)