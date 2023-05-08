package com.example.geograpgygame.model

@kotlinx.serialization.Serializable
data class Country(
    val name: CountryName,
    val ccn3: String,
    val capital: List<String>?,
    val region: String,
    val area: Double,
    val flag: String,
    val population: Int,

    ) {}