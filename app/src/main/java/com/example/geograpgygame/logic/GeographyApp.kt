package com.example.geograpgygame.logic

import com.example.geograpgygame.model.Country
import com.example.geograpgygame.model.question.Question
import com.example.geograpgygame.model.question.QuestionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object GeographyApp {
    val questionsMade = mutableListOf<Question>()
    var currentQuestion: Question? = null
    private val nextQuestions = mutableListOf<Question>()
    suspend fun makeQuestions() {
        if (nextQuestions.size < 5) {
            CoroutineScope(Dispatchers.Default).launch {
                while (nextQuestions.size < 5) {
                    nextQuestions.add(getNewQuestion())
                }
            }
        }
    }

    suspend fun setCurrentQuestion() {
        if (nextQuestions.isEmpty()){
            currentQuestion = getNewQuestion()
        } else {
            currentQuestion = nextQuestions.removeAt(0)
            makeQuestions()
        }
        println(currentQuestion!!)
    }

    private suspend fun getNewQuestion(): Question {
        var question: Question
        var questionType: QuestionType
        do {
            questionType = QuestionType.values().random()
            question = getQuestion(questionType)
        } while (questionsMade.contains(question) || nextQuestions.contains(question))
        return question
    }

    private suspend fun getQuestion(type: QuestionType): Question {
        val typeOptionsMap = mapOf(
            QuestionType.CAPITAL to { country: Country -> country.capital?.get(0) ?: "No capital" },
            QuestionType.FLAG to { country: Country -> country.flag },
            QuestionType.POPULATION to { country: Country -> country.population.toString() }
        )
        val typeTextMap = mapOf(
            QuestionType.CAPITAL to { country: Country -> "What is the capital of ${country.name.common}?" },
            QuestionType.FLAG to { country: Country -> "What is the flag of ${country.name.common}?" },
            QuestionType.POPULATION to { country: Country -> "What is the population of ${country.name.common}?" }
        )
        val country: Country= ServiceLocator.repository.getCountry()
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
        var country: Country
        var option: String
        options.add(answer)
        while (options.size < 4) {
                country = ServiceLocator.repository.getCountry()
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

    fun checkAnswer(selectedOption: Int):Boolean {
            return currentQuestion!!.answer == currentQuestion!!.options[selectedOption]
    }

    fun saveQuestion() {
        questionsMade.add(currentQuestion!!)
    }


}