package com.example.carmaintenance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(label: String, value: String, onInputChange: (String) -> Unit, singleLine: Boolean, type: String) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier.fillMaxSize(),
        value = value,
        onValueChange = onInputChange,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        label = { Text(label) },
        singleLine = singleLine,
        keyboardOptions = if (type == "Int") KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions()
    )
}