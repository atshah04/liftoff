package com.example.liftoff.ui.options

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.liftoff.ui.components.TextL
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import com.example.liftoff.R
import androidx.compose.foundation.Image

@Composable
fun OptionsScreen(navFuncs: Map<String, ()->Unit>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Column(modifier = Modifier.fillMaxWidth()
                                    .fillMaxHeight(0.8f), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_final1),
                    contentDescription = "Liftoff Logo",
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 16.dp)) {
                    Button(onClick = { navFuncs["login"]!!.invoke() }, Modifier.width(150.dp).height(50.dp)) {
                        Text("Log in")
                    }
                    Button(onClick = { navFuncs["newAcc"]!!.invoke() }, Modifier.width(150.dp).height(50.dp)) {
                        Text("Create Account")
                    }
                }
            }
        }
    }
}