package com.example.geograpgygame.logic

import com.example.geograpgygame.interfaces.RestCountriesApi
import com.example.geograpgygame.model.Country
import com.example.geograpgygame.model.question.Question
import com.example.geograpgygame.model.question.QuestionType
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

class GeographyApp {
    var countriesCodes: List<String> = mutableListOf()
    val questionsMade = mutableListOf<Question>()
    lateinit var currentQuestion: Question

    init {
        CoroutineScope(Dispatchers.Default).launch {
            countriesCodes = getCountriesCodes()
        }
    }


    @JvmName("getCountriesCodes1")
    private fun getCountriesCodes(): List<String> {
        val url = URL("https://restcountries.com/v3.1/all?fields=ccn3")
        //fetch data from url
        OkHttpClient().newCall(okhttp3.Request.Builder().url(url).build()).execute()
            .use { response ->
                val body = response.body()?.string()
                val codes =
                    body?.split("\"ccn3\":\"")?.drop(1)?.map { it.substring(0, 3) }?.toList()
                return codes ?: listOf()
            }
    }

    fun getCoutry(ccn3: String): Deferred<Country> {
        val url = "https://restcountries.com/v3.1/"
        //fetch data from url
        return CoroutineScope(Dispatchers.Default).async {
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestCountriesApi::class.java)
                .getCountry(ccn3).let { response ->
                    println(response)
                    response.body()!![0]

                }
        }
    }

    suspend fun makeQuestion(): Question {
        var question: Question
        do {
            question = getQuestion()
        } while (questionsMade.contains(question))
        currentQuestion = question
        return currentQuestion
    }

    private suspend fun getQuestion(): Question {
        val country = getCoutry(countriesCodes.random()).await()
        val questionType = QuestionType.values().random()
        val question = when (questionType) {
            QuestionType.CAPITAL -> {
                val options = mutableListOf<String>()
                options.add(country.capital[0])
                while (options.size < 4) {
                    val option = getCoutry(countriesCodes.random()).await().capital[0]
                    if (!options.contains(option)) {
                        options.add(getRandomPosition(options.size), option)
                    }
                }
                Question(
                    text = "What is the capital of ${country.name.common}?",
                    answer = country.capital[0],
                    answered = false,
                    correct = false,
                    options = options,
                    type = questionType
                )
            }
            QuestionType.FLAG -> {
                val options = mutableListOf<String>()
                options.add(country.flag)
                while (options.size < 4) {
                    val option = getCoutry(countriesCodes.random()).await().flag
                    if (!options.contains(option)) {
                        options.add(getRandomPosition(options.size), option)
                    }
                }
                Question(
                    text = "What is the flag of ${country.name.common}?",
                    answer = country.flag,
                    answered = false,
                    correct = false,
                    options = options,
                    type = questionType
                )
            }
            QuestionType.POPULATION -> {
                val options = mutableListOf<String>()
                options.add(country.population.toString())
                while (options.size < 4) {
                    val option = getCoutry(countriesCodes.random()).await().population.toString()
                    if (!options.contains(option)) {
                        options.add(option)
                    }
                }
                Question(
                    text = "What is the population of ${country.name.common}?",
                    answer = country.population.toString(),
                    answered = false,
                    correct = false,
                    options = options,
                    type = questionType
                )
            }
        }
        questionsMade.add(question)
        return question
    }

    private fun getRandomPosition(size: Int): Int {
        return (0..size).random()
    }


}