package com.example.olib_retry.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColors = lightColorScheme()

@Composable
fun OLibWorksTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColors,
        content = content
    )
}