package acidicoala.koalageddon.core.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object AppTheme {
    object AppColors {
        val Green = Color(0xFF00B000)
        val DarkGreen = Color(0xFF003000)
        val DarkGray = Color(0xFF202020)
        val LightGray = Color(0xFFE8E8E8)
        val Orange = Color(0xFFFF8000)
    }

    object Material {
        val darkColors = darkColors(
            primary = AppColors.Green,
            onPrimary = AppColors.DarkGreen,
            background = AppColors.DarkGray,
        )
        val lightColors = lightColors(
            primary = AppColors.Green,
            background = AppColors.LightGray,
            surface = AppColors.LightGray,
        )
    }
}