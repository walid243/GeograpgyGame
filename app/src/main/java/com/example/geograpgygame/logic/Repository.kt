package com.example.geograpgygame.logic

import com.example.geograpgygame.interfaces.IRepository
import com.example.geograpgygame.interfaces.RestCountriesApi
import com.example.geograpgygame.model.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository : IRepository {
    private val baseURL = "https://restcountries.com/v3.1/"
    private var countriesCodes = listOf<String>()
    override suspend fun loadCountryCodes() {
        countriesCodes =  fetchCountriesCodesAsync().await().map {
            it.values.first()
        }
    }

    override suspend fun getCountry(): Country {
        if (countriesCodes.isEmpty()) loadCountryCodes()
        var country: Country?
        do {
            country = fetchCountryAsync(countriesCodes.random()).await()!!
        } while (country == null)
        return country
    }

    override suspend fun fetchCountriesCodesAsync(): Deferred<List<Map<String, String>>> {
        //fetch data from url
        return CoroutineScope(Dispatchers.Default).async {
            Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestCountriesApi::class.java)
                .getCountriesCodes("ccn3")
                .body()!!
        }
    }

    override suspend fun fetchCountryAsync(ccn3: String): Deferred<Country?> {
        //fetch data from url
        return CoroutineScope(Dispatchers.Default).async {
            Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestCountriesApi::class.java)
                .getCountry(ccn3)
                .body()?.get(0)
        }
    }



}