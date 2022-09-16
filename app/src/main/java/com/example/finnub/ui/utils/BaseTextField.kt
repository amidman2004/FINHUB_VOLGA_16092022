package com.example.finnub.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.finnub.ui.theme.finnhubGreen

@Composable
fun BaseTextField(
    value:String,
    onValueChanged:(value:String) -> Unit,
    isPasswordField:Boolean = false,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier
        ,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            disabledBorderColor = finnhubGreen,
            focusedBorderColor = finnhubGreen,
            unfocusedBorderColor = finnhubGreen,
            cursorColor = finnhubGreen
        ),
        shape = RoundedCornerShape(50),
        visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
        textStyle = TextStyle(textAlign = TextAlign.Start)
    )
}