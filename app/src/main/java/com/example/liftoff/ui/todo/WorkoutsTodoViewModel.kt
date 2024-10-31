package com.example.liftoff.ui.todo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Stable
data class ExerciseTodo(
    val name: String,
    val type: String,
    val duration: Int? = null,
    val sets: Int? = null,
    val reps: Int? = null,
    val weight: Double? = null,
    var isDone: Boolean,
    val id: Int
)

class WorkoutsTodoViewModel () {
    var curId = 0
    val todoItems = mutableStateListOf<ExerciseTodo>();

    fun addWorkout(workout: ExerciseTodo) {
        todoItems.add(workout);
        curId++
    }
    fun removeTodo(workout: ExerciseTodo) {
        todoItems.remove(workout);
    }

    fun toggleComplete(workout: ExerciseTodo){
        val updated = ExerciseTodo(
            name = workout.name,
            type = workout.type,
            duration = workout.duration,
            sets = workout.sets,
            reps = workout.reps,
            weight = workout.weight,
            isDone = !workout.isDone,
            id = workout.id
        )

        val idx = todoItems.indexOf(workout)
        todoItems[idx] = updated
    }
}