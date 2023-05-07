package com.example.geograpgygame.model.question

class Question(
    val text : String,
    val answer : String,
    var answered : Boolean,
    var correct : Boolean,
    val options : List<String>,
    val type: QuestionType
){
    override fun toString(): String {
        return "Question(text='$text', answer='$answer', answered=$answered, correct=$correct, options=$options, type=$type)"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Question){
            return false
        }
        if (this.type != other.type){
            return false
        }
        if (this.answer != other.answer){
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + answer.hashCode()
        result = 31 * result + answered.hashCode()
        result = 31 * result + correct.hashCode()
        result = 31 * result + options.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
