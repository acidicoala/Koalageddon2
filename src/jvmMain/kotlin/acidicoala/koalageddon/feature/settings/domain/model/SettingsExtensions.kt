package acidicoala.koalageddon.feature.settings.domain.model

import acidicoala.koalageddon.core.ui.theme.AppTheme
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

val Settings.strings: Strings
    get() = when (language) {
        Settings.Language.English -> Strings.English
        Settings.Language.Russian -> Strings.Russian
    }

val Settings.colors: Colors
    @Composable get() = when (theme) {
        Settings.Theme.Dark -> AppTheme.Material.darkColors
        Settings.Theme.Light -> AppTheme.Material.lightColors
    }

val Settings.colorScheme: ColorScheme
    @Composable get() = when (theme) {
        Settings.Theme.Dark -> AppTheme.Material3.darkColorScheme
        Settings.Theme.Light -> AppTheme.Material3.lightColorScheme
    }
