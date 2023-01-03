package acidicoala.koalageddon.main.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultIconSize
import acidicoala.koalageddon.core.values.Bitmaps
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

sealed class MainTab(val label: @Composable () -> String, val icon: @Composable () -> Unit) {
    object Steam : MainTab(
        label = { LocalStrings.current.storeSteam },
        icon = {
            Image(
                painter = painterResource(Bitmaps.Steam),
                contentDescription = LocalStrings.current.storeSteam,
                modifier = Modifier.size(DefaultIconSize)
            )
        }
    )

    object Settings : MainTab(
        label = { LocalStrings.current.settings },
        icon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = LocalStrings.current.settings,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(DefaultIconSize)
            )
        }
    )
}