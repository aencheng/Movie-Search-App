package com.example.project8

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbService {
    @GET("/")
    fun searchMovies(
        @Query("t") title: String,
        @Query("apikey") key: String
    ): Call<MovieSearchResult>
}