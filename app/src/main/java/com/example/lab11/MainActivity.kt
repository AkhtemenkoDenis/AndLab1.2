package com.example.lab11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordScrambleGame()
        }
    }
}

@Composable
fun WordScrambleGame() {
    var shouldStartNewGame by remember { mutableStateOf(false) }
    val words = listOf("year", "apple", "mobile", "birthday", "homepage", "kotlin", "happy", "paper", "world", "game")
    var originalWord by remember { mutableStateOf(words.random()) }
    var scrambledWord by remember { mutableStateOf(shuffleWord(originalWord)) }
    var userAnswer by remember { mutableStateOf(TextFieldValue("")) }
    var resultMessage by remember { mutableStateOf("") }
    var resultColor by remember { mutableStateOf(Color.Transparent) }

    val keyboardController = LocalSoftwareKeyboardController.current

    fun newGame() {
        originalWord = words.random()
        scrambledWord = shuffleWord(originalWord)
        userAnswer = TextFieldValue("")
        resultMessage = ""
        resultColor = Color.Transparent
    }

    if (shouldStartNewGame) {
        LaunchedEffect(shouldStartNewGame) {
            kotlinx.coroutines.delay(1000)
            newGame()
            shouldStartNewGame = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F7FB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Word Scramble",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6D2DF6)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Scrambled word",
                style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = scrambledWord,
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444)),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(36.dp))

            OutlinedTextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text("Your answer") },
                textStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.85f),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (userAnswer.text.trim().equals(originalWord, ignoreCase = true)) {
                            resultMessage = "Correct! 🎉"
                            resultColor = Color(0xFF4BB543)
                            shouldStartNewGame = true
                        } else {
                            resultMessage = "WRONG!"
                            resultColor = Color(0xFFFF4455)
                        }
                        keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    if (userAnswer.text.trim().equals(originalWord, ignoreCase = true)) {
                        resultMessage = "Correct! 🎉"
                        resultColor = Color(0xFF4BB543)
                        shouldStartNewGame = true
                    } else {
                        resultMessage = "WRONG!"
                        resultColor = Color(0xFFFF4455)
                    }
                    keyboardController?.hide()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D2DF6)),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text("UNSCRAMBLE WORD!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (resultMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .background(resultColor, RoundedCornerShape(18.dp))
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = resultMessage,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

fun shuffleWord(word: String): String {
    if (word.length <= 1) return word
    var scrambled: String
    do {
        scrambled = word.toList().shuffled().joinToString("")
    } while (scrambled.equals(word, ignoreCase = true))
    return scrambled
}
