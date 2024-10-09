package com.example.liftoff.ui.todo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


data class ToDo(val name: String, var isDone: Boolean = false)

val default_todomod = Modifier
    .clip(RoundedCornerShape(2.dp))
    .padding(8.dp)
    .fillMaxWidth(0.9f)

class ToDoModel () {
    val todoItems = mutableStateListOf<ToDo>();

    fun addTodo(newItem: String) {
        todoItems.add(ToDo(newItem));
    }
    fun removeTodo(ToDoItem: ToDo) {
        todoItems.remove(ToDoItem);
    }

    fun toggleComplete(ToDoItem: ToDo){
        ToDoItem.isDone = !ToDoItem.isDone;
    }
}

@Composable
fun ToDoItemRow (item : ToDo, viewModel : ToDoModel) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    )
    {
        Text (
            text = if (item.isDone) "[X] ${item.name}" else "[ ] ${item.name}",
            modifier = Modifier.weight(1f)
        )
        Button(onClick = {viewModel.removeTodo(item)}) {
            Text("Delete")
        }
    }
}

@Composable
fun TodoScreen() {
    val TodoViewModel = remember {ToDoModel()};
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
        Text(
            text = "To Do",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        )
        {
            TextField(
                modifier = Modifier.weight(1f),
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Add New Task") }
            )
            Button(onClick = {
                    TodoViewModel.addTodo(textState.text)
                    textState = TextFieldValue("") // Clear the input field
            }) {
                Text("Add Task")
            }
        }

        LazyColumn {
            items(TodoViewModel.todoItems) { item ->
                ToDoItemRow(item, TodoViewModel)
            }
        }
    }
}
