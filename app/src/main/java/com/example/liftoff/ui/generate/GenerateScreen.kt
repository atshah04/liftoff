package com.example.liftoff.ui.generate

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.core.text.isDigitsOnly
import com.example.liftoff.R
import com.example.liftoff.data.viewmodel.GenerateViewModel
import com.example.liftoff.ui.navigation.BottomNavItem
import com.example.liftoff.ui.todo.ExerciseTodo
import com.example.liftoff.ui.todo.WorkoutsTodoViewModel
import org.w3c.dom.Text

@Composable
fun GenerateScreen(navFuncs: Map<String, ()->Unit>, gvm: GenerateViewModel, tdvm: WorkoutsTodoViewModel) {
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
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Image(
                    painter = painterResource(id = R.drawable.rocket_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.size(50.dp),
                )

                Image(
                    painter = painterResource(id = R.drawable.rocket_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.size(50.dp),
                )

                Image(
                    painter = painterResource(id = R.drawable.rocket_emoji),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.size(50.dp),
                )

            }

            TextH("Generate Workout")
            Generate(navFuncs, gvm, tdvm)
        }
    }
}

fun validate_inputs(weight: TextFieldValue, minutes: TextFieldValue): Boolean {
    if (!weight.text.isDigitsOnly()) return false
    if (!minutes.text.isDigitsOnly()) return false
    return weight.text.isNotEmpty() && minutes.text.isNotEmpty()
}

@Composable
fun Generate(navFuncs: Map<String, ()->Unit>, gvm: GenerateViewModel, tdvm: WorkoutsTodoViewModel) {
    val Exercices = gvm.Exercices
    val weight by gvm.weight.collectAsState()
    val miniutesInput by gvm.minutesInput.collectAsState()
    val checkedStates by gvm.checkedStates.collectAsState()
    val error by gvm.error.collectAsState()
    val override by gvm.override.collectAsState()
    val setminutesInput = { input: TextFieldValue -> gvm.setMinutesInput(input) }
    val setCheckedState = { index: Int, state: Boolean -> gvm.setCheckedState(index, state) }
    val setWeight = { input: TextFieldValue -> gvm.setWeightInput(input) }
    val setError = { err: Boolean -> gvm.setError(err) }
    val setOverride = { over: Boolean -> gvm.setOverride(over) }
    val exportTodo = { numChecked: Int ->
        tdvm.todoItems.clear()
        for (i in 0..Exercices.size-1) {
            if (checkedStates[i]) {
                tdvm.todoItems.add(
                    ExerciseTodo(
                        Exercices[i],
                        "Strength",
                        null,
                        miniutesInput.text.toInt() / numChecked,
                        5,
                        weight.text.toDouble(),
                        false,
                        1
                    )
                )
            }
        }
        setminutesInput(TextFieldValue(""))
        setWeight(TextFieldValue(""))
        navFuncs[BottomNavItem.Todo.route]!!.invoke()
    }

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
        ) {
            TextS("Minutes for workout")
            DefaultTextField(miniutesInput, setminutesInput, "Minutes")
            TextS("Weight per exercise (kg)")
            DefaultTextField(weight, setWeight, "Weight")
            val stringminutes = miniutesInput.text
            val minutesInt = stringminutes.toIntOrNull()
            Text(
                text = "Check the Workouts That You Want:",
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
            Button(onClick = {
                if (!validate_inputs(weight, miniutesInput)) {
                    setError(true)
                } else {
                    var numChecked = 0
                    for (i in 0..Exercices.size-1) {
                        if (checkedStates[i]) numChecked += 1
                    }
                    if (numChecked == 0) {
                        setError(true)
                    } else if (tdvm.todoItems.size > 0) {
                        setOverride(true)
                    } else {
                        exportTodo(numChecked)
                    }
                }
            }) {
                Text("Export to Todo")
            }
            if (override) {
                Modal(
                    { setOverride(false)},
                    { setOverride(false)
                        var numChecked = 0
                        for (i in 0..Exercices.size-1) {
                            if (checkedStates[i]) numChecked += 1
                        }
                        exportTodo(numChecked)
                    },
                    "WARNING",
                    "Exporting this list to todo will override your current todo list, are you sure you want to proceed?",
                    Icons.Default.Info
                )
            }
            if (error) {
                Modal(
                    { setError(false)},
                    { setError(false)},
                    "Invalid fields entered",
                    "Weight and time must be non-negative, non-empty and you must select at least one workout.",
                    Icons.Default.Info
                )
            }
        }
    }

}

