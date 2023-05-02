package com.example.geograpgygame.model

@kotlinx.serialization.Serializable
class Country(
    val name: CountryName,
    val ccn3: String,
    val capital: List<String>,
    val region: String,
    val area: Int,
    val flag: String,
    val population: Int,

    ) {}