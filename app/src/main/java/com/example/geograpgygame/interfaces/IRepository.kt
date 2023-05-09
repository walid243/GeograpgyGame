package com.example.geograpgygame.interfaces

import com.example.geograpgygame.model.Country
import kotlinx.coroutines.Deferred

interface IRepository {

    suspend fun loadCountryCodes()
    suspend fun getCountry() : Country
    suspend fun fetchCountriesCodesAsync(): Deferred<List<Map<String, String>>>
    suspend fun fetchCountryAsync(ccn3: String): Deferred<Country?>

}