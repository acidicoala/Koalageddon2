package acidicoala.koalageddon.core.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppTheme {
    object AppColors {
        val Green = Color(0xFF00C000)
        val DarkGreen = Color(0xFF002000)
    }

    object Material {
        val darkColors = darkColors(
            primary = AppColors.Green,
            onPrimary = AppColors.DarkGreen,
        )
        val lightColors = lightColors(
            primary = AppColors.Green,
            onPrimary = AppColors.DarkGreen,
        )
    }

    object Material3 {
        val darkColorScheme = darkColorScheme(
            primary = AppColors.Green,
            onPrimary = AppColors.DarkGreen,
        )
        val lightColorScheme = lightColorScheme(
            primary = AppColors.Green,
            onPrimary = AppColors.DarkGreen,
        )
    }
}