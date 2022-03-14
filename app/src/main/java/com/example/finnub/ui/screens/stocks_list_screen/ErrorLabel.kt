package com.example.finnub.ui.screens.stocks_list_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ErrorLabel(
    error:String,
    isError:Boolean) {
    AnimatedVisibility(visible = isError,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)){
            Text(text = "An Error Occurred, $error",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.Center))
        }
    }
}