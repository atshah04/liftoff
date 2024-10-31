package com.example.liftoff.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.liftoff.data.classes.Users
import com.example.liftoff.ui.home.Card

@Composable
fun UserColumn(items: List<Users>) {
    Column {
        items.forEach { session ->
            Card(session)
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}