package acidicoala.koalageddon.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppTheme {
    object AppColors {
        val GreenLight = Color(0xFF00B000)
        val Green = Color(0xFF009000)
        val GreenDark = Color(0xFF003000)
        val DarkerGray = Color(0xFF111111)
        val DarkGray = Color(0xFF222222)
        val LightGray = Color(0xFFDDDDDD)
        val LighterGray = Color(0xFFEEEEEE)
        val Orange = Color(0xFFFF8000)
    }

    object Material {
        val darkColors = darkColors(
            primary = AppColors.GreenLight,
            onPrimary = AppColors.GreenDark,
            background = AppColors.DarkerGray,
            surface = AppColors.DarkGray,
            secondaryVariant = AppColors.GreenLight,
        )
        val lightColors = lightColors(
            primary = AppColors.Green,
            background = AppColors.LighterGray,
            surface = AppColors.LightGray,
            secondaryVariant = AppColors.Green,
        )
    }

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(12.dp),
    )
}