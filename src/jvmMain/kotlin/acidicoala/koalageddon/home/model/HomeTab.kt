package acidicoala.koalageddon.home.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Bitmaps
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

enum class HomeTab(
    val label: @Composable () -> String,
    val painter: @Composable () -> Painter
) {
    Steam(
        label = { LocalStrings.current.storeSteam },
        painter = { painterResource(Bitmaps.Steam) },
    ),

    Settings(
        label = { LocalStrings.current.settings },
        painter = { painterResource(Bitmaps.Settings) },
    );
}