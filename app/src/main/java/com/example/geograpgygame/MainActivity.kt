package com.example.geograpgygame

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.geograpgygame.logic.GeographyApp
import com.example.geograpgygame.ui.theme.GeograpgyGameTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun App() {
    val geographyApp = GeographyApp()
    var questionText: String = "Aun no hay pregunta"
    while (geographyApp.countriesCodes.isEmpty()) {
        //wait for countriesCodes to be fetched
    }
    CoroutineScope(Dispatchers.Default).launch {
//        geographyApp.makeQuestion()
//             questionText = geographyApp.currentQuestion.text
    }
    Text(text = questionText)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GeograpgyGameTheme {
        App()
    }
}