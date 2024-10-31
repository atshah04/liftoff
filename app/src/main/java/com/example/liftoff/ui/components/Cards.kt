package com.example.liftoff.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liftoff.ui.todo.ExerciseTodo

@Composable
fun WorkoutTodoCard (item: ExerciseTodo) {
    Card (
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors (
            containerColor = if (item.isDone) Color(0xFF98EC99) else Color.White
        )) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (item.type == "timed") {
                Text (
                    text = "Duration: ${item.duration} mins",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            } else {
                Text (
                    text = "Sets: ${item.sets}          Reps: ${item.reps}          Weight: ${item.weight} lbs",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
