package com.example.liftoff.ui.options

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.liftoff.data.classes.GlobalState
import com.example.liftoff.ui.components.TextL
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.liftoff.R
import androidx.compose.foundation.Image

@Composable
fun OptionsScreen(navFuncs: Map<String, ()->Unit>, setGlobals: (GlobalState) -> Unit) {
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
            TextL("LiftOff")
            Image(
                painter = painterResource(id = R.drawable.liftoff_logo3),
                contentDescription = "Liftoff Logo",
                modifier = Modifier.fillMaxWidth(),
//                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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