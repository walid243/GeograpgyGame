package com.example.geograpgygame.model.question

class Question(
    val text : String,
    val answer : String,
    val answered : Boolean,
    val correct : Boolean,
    val options : List<String>,
    val type: QuestionType
)
