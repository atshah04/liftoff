package com.example.liftoff.ui.generate

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liftoff.data.classes.GlobalState
import com.example.liftoff.data.classes.User
import com.example.liftoff.data.classes.User_
import com.example.liftoff.data.classes.Users
import com.example.liftoff.ui.components.*
import com.example.liftoff.ui.login.LoginPage
import com.example.liftoff.ui.login.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.*
import com.example.liftoff.data.viewmodel.GenerateViewModel

@Composable
fun GenerateScreen(navFuncs: Map<String, ()->Unit>, gvm: GenerateViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Text(
                text = "Generate Workout",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,

            )
            Generate(navFuncs, gvm)
        }
    }
}

@Composable
fun Generate(navFuncs: Map<String, ()->Unit>, gvm: GenerateViewModel) {
    val Exercices = gvm.Exercices
    val miniutesInput by gvm.minutesInput.collectAsState()
    val checkedStates by gvm.checkedStates.collectAsState()
    val setminutesInput = { input: TextFieldValue -> gvm.setMinutesInput(input) }
    val setCheckedState = { index: Int, state: Boolean -> gvm.setCheckedState(index, state) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            Text(
                text = "How Many Minutes Do You Want The Workout To Be?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center

            )
            DefaultTextField(miniutesInput, setminutesInput)
            Row(
                Modifier
                    .width(250.dp)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(onClick = {


                }) {
                    Text("How Many Minutes Do You Want The Workout To Be ?")
                }
            }
            val stringminutes = miniutesInput.text
            val minutesInt = stringminutes.toIntOrNull()

            Text(
                text = "Check the Workouts That You Want!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 18.dp)

            )

            Column {
                checkedStates.forEachIndexed { index, part ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { isChecked ->
                                setCheckedState(index, isChecked)
                            }
                        )

                        Text(
                            text = Exercices[index],
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 18.sp
                        )


                    }
                }
            }

            val selectedParts = mutableListOf<String>()
            for ((index, part) in Exercices.withIndex()) {
                if (checkedStates[index]) {
                    selectedParts.add(part)
                }
            }

            Text(text = "Selected Exercises: ${selectedParts.joinToString(", ")}")


            }

        }

    }

