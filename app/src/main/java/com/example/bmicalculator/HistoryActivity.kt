package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val listOfBmiHistory = MainActivity.listOfBmiHistory
            val formatter = SimpleDateFormat("dd-MMMM-yyyy h:mm a")

            Column(modifier = Modifier.padding(20.dp)) {
                Header(text = "WebDev BMI Calculator - BMI History")


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(0.dp, 25.dp, 0.dp, 0.dp)
                ) {
                    if (listOfBmiHistory.size == 0) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                        ) {
                            Text(text = "No history at the moment...")
                        }
                    }

                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(listOfBmiHistory.size) { index ->
                            run {
                                if (index == 0) Divider()
                                Column(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 10.dp)) {
                                    Text(text = "BMI: ${listOfBmiHistory[index].bmi} (${listOfBmiHistory[index].bmiStatus})")
                                    Text(text = "Date: ${formatter.format(listOfBmiHistory[index].date)}")
                                }
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}