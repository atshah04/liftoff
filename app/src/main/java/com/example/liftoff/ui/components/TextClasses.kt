package com.example.liftoff.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextL(txt: String) {
    Text(
        text = txt,
        fontSize = 64.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun TextH(txt: String) {
    Text(
        text = txt,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End,
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun DefaultTextField(str: TextFieldValue, fn: (TextFieldValue) -> Unit, placeholder: String = "") {
    OutlinedTextField(
        value = str,
        onValueChange = { fn(it) },
        placeholder = { Text(text = placeholder) },
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
    )
}

@Composable
fun PassTextField(str: TextFieldValue, fn: (TextFieldValue) -> Unit, placeholder: String = "") {
    OutlinedTextField(
        value = str,
        onValueChange = { fn(it) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = { Text(text = placeholder) },
    )
}

