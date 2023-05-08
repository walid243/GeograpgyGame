package com.example.geograpgygame.model.question

data class Question(
    val text : String,
    val answer : String,
    var answered : Boolean,
    var correct : Boolean,
    val options : List<String>,
    val type: QuestionType
){


}
