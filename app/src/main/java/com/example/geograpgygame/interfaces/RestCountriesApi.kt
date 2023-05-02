package com.example.geograpgygame.interfaces

import com.example.geograpgygame.model.Country
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestCountriesApi {
    @GET("alpha")
    suspend fun getCountry(@Query("codes") codes: String): Response<Country>
}