package com.devflow.app.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(

    primary = Primary,
    onPrimary = OnPrimary,

    secondary = Secondary,
    onSecondary = OnSecondary,

    background = Background,
    onBackground = OnBackground,

    surface = Surface,
    onSurface = OnSurface,

    error = Error,

    outline = Outline
)

@Composable
fun DevFlowTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme = LightColors,

        typography = AppTypography,

        shapes = AppShapes,

        content = content
    )
}