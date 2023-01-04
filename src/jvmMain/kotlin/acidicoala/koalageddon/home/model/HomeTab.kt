package acidicoala.koalageddon.home.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultIconSize
import acidicoala.koalageddon.core.values.Bitmaps
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

enum class HomeTab(
    val label: @Composable () -> String,
    val icon: @Composable () -> Unit
) {
    Steam(
        label = { LocalStrings.current.storeSteam },
        icon = {
            Image(
                painter = painterResource(Bitmaps.Steam),
                contentDescription = LocalStrings.current.storeSteam,
                modifier = Modifier.size(DefaultIconSize)
            )
        }
    ),

    Settings(
        label = { LocalStrings.current.settings },
        icon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = LocalStrings.current.settings,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(DefaultIconSize)
            )
        }
    );
}