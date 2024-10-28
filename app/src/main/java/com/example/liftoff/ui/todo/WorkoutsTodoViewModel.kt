package com.example.liftoff.ui.todo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

data class ExerciseTodo(
    val name: String,
    val type: String,
    val duration: Int? = null,
    val sets: Int? = null,
    val reps: Int? = null,
    val weight: Double? = null,
    var isDone: Boolean
)

class WorkoutsTodoViewModel () {
    val todoItems = mutableStateListOf<ExerciseTodo>();

    fun addWorkout(workout: ExerciseTodo) {
        todoItems.add(workout);
    }
    fun removeTodo(workout: ExerciseTodo) {
        todoItems.remove(workout);
    }

    fun toggleComplete(workout: ExerciseTodo){
        workout.isDone = !workout.isDone;
    }
}