package com.example.geograpgygame.logic

import com.example.geograpgygame.model.CountryData
import com.example.geograpgygame.model.question.Question
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import java.net.URL

class GeographyApp {
    private val countriesCodes = getCountriesCodes()
    val countriesSaved = mutableListOf<String>()
    val questionsMade = mutableListOf<Question>()
    var currentQuestion: Question? = null

    private val json = Json { ignoreUnknownKeys = true }

    fun getCountriesCodes(): List<String>{
        val url = URL("https://restcountries.com/v3.1/all?fields=ccn3")
        //fetch data from url
        val response = fetch(url)
    }
    fun fetch(url:URL): CountryData {
        return Retrofit.Builder()
            .baseUrl(url)
            .build()
            .create(CountryData::class.java)
    }


}