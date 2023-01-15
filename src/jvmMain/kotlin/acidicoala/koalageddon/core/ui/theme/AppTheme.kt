package acidicoala.koalageddon.core.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object AppTheme {
    object AppColors {
        val GreenLight = Color(0xFF00B000)
        val Green = Color(0xFF009000)
        val GreenDark = Color(0xFF003000)
        val DarkGray = Color(0xFF202020)
        val LightGray = Color(0xFFE8E8E8)
        val Orange = Color(0xFFFF8000)
    }

    object Material {
        val darkColors = darkColors(
            primary = AppColors.GreenLight,
            onPrimary = AppColors.GreenDark,
            background = AppColors.DarkGray,
            secondaryVariant = AppColors.GreenLight,
        )
        val lightColors = lightColors(
            primary = AppColors.Green,
            background = AppColors.LightGray,
            surface = AppColors.LightGray,
            secondaryVariant = AppColors.Green,
        )
    }
}