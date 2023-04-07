package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmicalculator.model.BmiHistoryModel
import com.example.bmicalculator.model.BmiStatusModel
import com.example.bmicalculator.ui.theme.BMICalculatorTheme
import java.util.*

class MainActivity : ComponentActivity() {

    companion object {
        var listOfBmiHistory: MutableList<BmiHistoryModel> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var listOfBmiStatus = BmiStatusModel()

        setContent {
            val context = LocalContext.current
            BMICalculatorTheme {

                var weight by remember {
                    mutableStateOf("00")
                }

                var height by remember {
                    mutableStateOf("00")
                }

                var bmi by remember {
                    mutableStateOf("0.0")
                }

                var gender by remember {
                    mutableStateOf("")
                }


                fun updateBmi() {
                    if (height.isNotEmpty() && weight.isNotEmpty() && height.toInt() > 30) {
                        val heightToMeter = (height.toDouble() * 0.01)
                        val result = weight.toDouble() / (heightToMeter * heightToMeter)

                        bmi = (Math.round(result * 100.0) / 100.0).toString()

                        val intBmi = bmi.toDouble()

                        listOfBmiStatus = when (intBmi) {
                            in 0.0..18.49 -> BmiStatusModel("Underweight", Color.Blue)
                            in 18.5..24.99 -> BmiStatusModel("Healthy", Color.Green)
                            in 25.00..29.99 -> BmiStatusModel("Overweight", Color.Blue)
                            in 30.00..200.00 -> BmiStatusModel("Obese", Color.Red)
                            else -> BmiStatusModel("Invalid", Color.Red)
                        }

                        val temp = BmiHistoryModel(
                            weight,
                            height,
                            bmi,
                            listOfBmiStatus.status,
                            listOfBmiStatus.color,
                            Date()
                        )
                        listOfBmiHistory.add(temp)
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Header(text = "WebDev BMI Calculator")


                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1F)
                            ) {
                                Card(cardImage = R.drawable.ic_boy,
                                    textTitle = "Weight (kg)",
                                    value = weight,
                                    handleValueChange = { weight = it; updateBmi() },
                                    cardGender = "Male",
                                    gender = gender,
                                    handleGenderChange = { gender = it })
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1F)
                            ) {
                                Card(cardImage = R.drawable.ic_girl,
                                    textTitle = "Height (cm)",
                                    value = height,
                                    handleValueChange = { height = it; updateBmi() },
                                    cardGender = "Female",
                                    gender = gender,
                                    handleGenderChange = { gender = it })
                            }
                        }


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 50.dp, 0.dp, 0.dp)
                        ) {
                            BmiResult(bmi = bmi, bmiStatusModel = listOfBmiStatus)

                            Button(onClick = {
                                bmi = "0.0"
                                weight = "00"
                                height = "00"
                                gender = ""
                                listOfBmiStatus = BmiStatusModel()
                            }) {
                                Text(text = "Reset Form", fontSize = 18.sp)
                            }

                            TextButton(onClick = {
                                context.startActivity(
                                    Intent(
                                        context, HistoryActivity::class.java
                                    )
                                )
                            }) {
                                Text(text = "BMI History",
                                    color = Color.Blue,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .padding(0.dp, 10.dp, 0.dp, 0.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Card(
        cardImage: Int,
        textTitle: String,
        value: String,
        handleValueChange: (String) -> Unit,
        cardGender: String,
        gender: String,
        handleGenderChange: (String) -> Unit,
    ) {
        Image(painter = painterResource(id = cardImage),
            contentDescription = "",
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 30.dp)
                .clickable {
                    handleGenderChange(cardGender)
                }
                .then(
                    if (cardGender === gender) {
                        Modifier.border(
                            BorderStroke(
                                1.dp, Color.Green
                            )
                        )
                    } else {
                        Modifier.border(BorderStroke(0.dp, Color.Transparent))
                    }
                ))

        Text(
            text = textTitle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)
        )

        BasicTextField(value = value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = {
                handleValueChange(it.filter { symbol ->
                    symbol.isDigit() || symbol == '.'
                })
            },
            textStyle = TextStyle.Default.copy(
                fontSize = 75.sp, textAlign = TextAlign.Center
            ),
            modifier = Modifier.clickable {
                if (value.toInt() == 0) handleValueChange("")
            })
    }

    @Composable
    fun BmiResult(bmi: String, bmiStatusModel: BmiStatusModel) {
        Text(text = "Your BMI")
        Text(text = bmi, fontSize = 60.sp, fontWeight = FontWeight.Bold)
        Text(
            text = bmiStatusModel.status,
            color = bmiStatusModel.color,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )
    }

}

@Composable
fun Header(text: String) {
    Text(text = text, fontSize = 25.sp)
}
