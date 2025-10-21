package com.example.mycalcy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var currentOperator by remember { mutableStateOf<String?>(null) }
    var previousNumber by remember { mutableStateOf<Double?>(null) }
    var isNewNumber by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF2E2E2E)) // DarkGray
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = display,
            fontSize = 72.sp,
            color = Color.White,
            textAlign = TextAlign.End,
            maxLines = 1,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Text(
            text = "Developer : Aalhad M. Ramteke",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        CalculatorRow(
            buttons = listOf("AC", "+/-", "%", "÷"),
            onButtonClick = { button ->
                when (button) {
                    "AC" -> {
                        display = "0"
                        currentOperator = null
                        previousNumber = null
                        isNewNumber = true
                    }
                    "+/-" -> {
                        val number = display.toDoubleOrNull()
                        if (number != null && number != 0.0) {
                            display = (number * -1).toString()
                        }
                    }
                    "%" -> {
                        val number = display.toDoubleOrNull()
                        if (number != null) {
                            display = (number / 100).toString()
                        }
                    }
                    "÷" -> {
                        currentOperator = button
                        previousNumber = display.toDoubleOrNull()
                        isNewNumber = true
                    }
                }
            }
        )

        CalculatorRow(
            buttons = listOf("7", "8", "9", "x"),
            onButtonClick = { button ->
                when (button) {
                    "x" -> {
                        currentOperator = button
                        previousNumber = display.toDoubleOrNull()
                        isNewNumber = true
                    }
                    else -> {
                        if (isNewNumber) display = ""
                        display += button
                        isNewNumber = false
                    }
                }
            }
        )

        CalculatorRow(
            buttons = listOf("4", "5", "6", "-"),
            onButtonClick = { button ->
                when (button) {
                    "-" -> {
                        currentOperator = button
                        previousNumber = display.toDoubleOrNull()
                        isNewNumber = true
                    }
                    else -> {
                        if (isNewNumber) display = ""
                        display += button
                        isNewNumber = false
                    }
                }
            }
        )

        CalculatorRow(
            buttons = listOf("1", "2", "3", "+"),
            onButtonClick = { button ->
                when (button) {
                    "+" -> {
                        currentOperator = button
                        previousNumber = display.toDoubleOrNull()
                        isNewNumber = true
                    }
                    else -> {
                        if (isNewNumber) display = ""
                        display += button
                        isNewNumber = false
                    }
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {            CalculatorButton(
            text = "0",
            backgroundColor = Color(0xFFA5A5A5), // LightGray
            modifier = Modifier
                .weight(2f)
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .clickable {
                    if (isNewNumber) display = ""
                    display += "0"
                    isNewNumber = false
                }
        )

            CalculatorButton(
                text = ".",
                backgroundColor = Color(0xFFA5A5A5), // LightGray
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable {
                        if (isNewNumber) {
                            display = "0."
                            isNewNumber = false
                        } else if (!display.contains(".")) {
                            display += "."
                        }                    }
            )

            CalculatorButton(
                text = "=",
                backgroundColor = Color(0xFFFFA500), // Orange
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable {
                        val currentNumber = display.toDoubleOrNull()
                        if (currentNumber != null && previousNumber != null && currentOperator != null) {
                            val result = when (currentOperator) {
                                "+" -> previousNumber!! + currentNumber
                                "-" -> previousNumber!! - currentNumber
                                "x" -> previousNumber!! * currentNumber
                                "÷" -> {
                                    if (currentNumber != 0.0) previousNumber!! / currentNumber else Double.NaN
                                }
                                else -> currentNumber
                            }
                            display = result.toString()
                            previousNumber = result
                            isNewNumber = true
                        }
                    }
            )
        }
    }
}

@Composable
fun CalculatorRow(buttons: List<String>, onButtonClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        buttons.forEach { button ->
            CalculatorButton(
                text = button,
                backgroundColor = when (button) {
                    "÷", "x", "-", "+" -> Color(0xFFFFA500) // Orange
                    else -> Color(0xFFA5A5A5) // LightGray
                },
                modifier = Modifier
                    .weight(1f)
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable { onButtonClick(button) }
            )
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            color = Color.White
        )
    }
}