package com.devflow.app.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppButton(

    text: String,

    onClick: () -> Unit,

    modifier: Modifier = Modifier,

    enabled: Boolean = true

) {

    Button(

        onClick = onClick,

        enabled = enabled,

        modifier = modifier.fillMaxWidth()

    ) {

        Text(text)

    }

}