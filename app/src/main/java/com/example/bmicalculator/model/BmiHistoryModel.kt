package com.example.bmicalculator.model

import androidx.compose.ui.graphics.Color
import java.util.*

data class BmiHistoryModel(
    val weight: String,
    val height: String,
    val bmi: String,
    val bmiStatus: String,
    val bmiColor: Color,
    val date: Date
)