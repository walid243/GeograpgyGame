package com.example.geograpgygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.geograpgygame.logic.GeographyApp
import com.example.geograpgygame.ui.theme.GeograpgyGameTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeograpgyGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun App() {
        println("Se lanza la pregunta <-------------")
        var currentQuestion by remember { mutableStateOf(GeographyApp.currentQuestion) }
        var questionText by remember { mutableStateOf("Waiting for question... ") }
        var options by remember { mutableStateOf(listOf<String>()) }
        var selectedOption by remember { mutableStateOf(-1) }
        var correctAnswers = GeographyApp.getCorrectQuestionsCount()
        LaunchedEffect(Unit){
            withContext(Dispatchers.IO) {
                GeographyApp.loadCountryCodes()
                GeographyApp.makeQuestion()
                if (GeographyApp.currentQuestion != null) {
                    currentQuestion = GeographyApp.currentQuestion
                    questionText = currentQuestion!!.text
                    options = currentQuestion!!.options
                }
            }
        }



        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Row() {
                Text(text = questionText)
            }
            if (GeographyApp.currentQuestion != null) {
                LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                    items(options.size) { index ->
                        Button(
                            onClick = {
                                if (!currentQuestion!!.answered){
                                selectedOption = index

                                currentQuestion = GeographyApp.currentQuestion
                                val newQuestion = currentQuestion!!.copy(answered = true, correct = checkAnswer(selectedOption))
                                GeographyApp.questionsMade.add(GeographyApp.currentQuestion!!)
                                currentQuestion = newQuestion
                                    if (currentQuestion!!.correct) correctAnswers += 1
                                }
                            },
                            modifier = Modifier.padding(16.dp),
                            colors = ButtonDefaults.let {
                                println(currentQuestion!!.answered)
                                if (currentQuestion!!.answered ) {
                                    if (currentQuestion!!.options[index] == currentQuestion!!.answer) {
                                        it.buttonColors(backgroundColor = Color.Green)
                                    } else {
                                        it.buttonColors(backgroundColor = Color.Red)
                                    }
                                } else {
                                    it.buttonColors(backgroundColor = Color.Blue)
                                }
                            }
                        ) {
                            Text(text = options[index])
                        }
                    }
                }
            } else {
                Text(text = "Waiting for Options...")
            }
            Row() {
                Text(
                    text = "Correct Answers: $correctAnswers",
                    modifier = Modifier.background(Color.Green)
                )
            }
            Row() {
                Button(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        GeographyApp.makeQuestion()
                        if (GeographyApp.currentQuestion != null) {
                            currentQuestion = GeographyApp.currentQuestion
                            questionText = currentQuestion!!.text
                            options = currentQuestion!!.options
                        }
                    } }) {
                    Text(text = "Next Question")
                }
            }
        }

    }

    fun checkAnswer(selectedOption: Int): Boolean {
        return GeographyApp.checkAnswer(selectedOption)
    }

}