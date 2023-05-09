package com.example.geograpgygame.interfaces

import com.example.geograpgygame.model.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestCountriesApi {
    @GET("alpha")
    suspend fun getCountry(@Query("codes") codes: String): Response<List<Country>>

    @GET("all")
    suspend fun getCountriesCodes(@Query("fields") code: String = "ccn3"): Response<List<Map<String, String>>>
}