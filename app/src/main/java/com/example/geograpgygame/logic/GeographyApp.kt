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

object GeographyApp {
    var countriesCodes: List<String> = mutableListOf()
    val questionsMade = mutableListOf<Question>()
    var currentQuestion: Question? = null

    init {
        CoroutineScope(Dispatchers.Default).launch {
            do {
                countriesCodes = getCountriesCodes()
            } while (countriesCodes.isEmpty())
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

    private fun getCountryAsync(ccn3: String): Deferred<Country?> {
        val url = "https://restcountries.com/v3.1/"
        //fetch data from url
        return CoroutineScope(Dispatchers.Default).async {
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestCountriesApi::class.java)
                .getCountry(ccn3)
                .body()?.get(0)
        }
    }

    suspend fun makeQuestion() {
        var question: Question
        var questionType: QuestionType
        do {
            questionType = QuestionType.values().random()
            question = getQuestion(questionType)
        } while (questionsMade.contains(question))
        currentQuestion = question
        println(currentQuestion)
    }

    private suspend fun getQuestion(type: QuestionType): Question {
        val typeOptionsMap = mapOf(
            QuestionType.CAPITAL to { country: Country -> country.capital[0] },
            QuestionType.FLAG to { country: Country -> country.flag },
            QuestionType.POPULATION to { country: Country -> country.population.toString() }
        )
        val typeTextMap = mapOf(
            QuestionType.CAPITAL to { country: Country -> "What is the capital of ${country.name.common}?" },
            QuestionType.FLAG to { country: Country -> "What is the flag of ${country.name.common}?" },
            QuestionType.POPULATION to { country: Country -> "What is the population of ${country.name.common}?" }
        )
        var country: Country?
        do {
            country = getCountryAsync(countriesCodes.random()).await()
        } while (country == null)
        return Question(
            text = typeTextMap[type]!!.invoke(country),
            answer = typeOptionsMap[type]!!.invoke(country),
            answered = false,
            correct = false,
            options = getOptions(
                type, typeOptionsMap,
                typeOptionsMap[type]!!.invoke(country)
            ),
            type = type
        )
    }

    private fun getRandomPosition(size: Int): Int {
        return (0..size).random()
    }

    private suspend fun getOptions(
        questionType: QuestionType,
        typeOptionsMap: Map<QuestionType, (Country) -> String>,
        answer: String
    ): List<String> {

        val options = mutableListOf<String>()
        var country: Country?
        var option = ""
        options.add(answer)
        while (options.size < 4) {
            do {
                country = getCountryAsync(countriesCodes.random()).await()

            } while (country == null)
            option = typeOptionsMap[questionType]!!.invoke(country)
            if (!options.contains(option)) {
                options.add(getRandomPosition(options.size), option)
            }
        }
        return options
    }

    fun getCorrectQuestionsCount(): Int {
        return questionsMade.filter { it.correct }.size
    }

    fun checkAnswer(): Boolean {
        currentQuestion!!.answered = true
        currentQuestion!!.correct = currentQuestion!!.answer == currentQuestion!!.options[0]
        questionsMade.add(currentQuestion!!)
        return currentQuestion!!.correct
    }

}