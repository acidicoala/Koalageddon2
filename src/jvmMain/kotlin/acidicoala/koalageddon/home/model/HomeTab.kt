package acidicoala.koalageddon.home.model

import acidicoala.koalageddon.core.model.Store
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Bitmaps
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import kotlinx.serialization.Serializable


@Serializable
enum class HomeTab(
    val priority: Int,
    val store: Store?,
    val label: @Composable () -> String,
    val painter: @Composable () -> Painter,
) {
    Start(
        priority = 0,
        store = null,
        label = { LocalStrings.current.startPage },
        painter = { painterResource(Bitmaps.Icon) },
    ),
    Settings(
        priority = 1,
        store = null,
        label = { LocalStrings.current.settings },
        painter = { painterResource(Bitmaps.Settings) },
    ),
    Epic(
        priority = 100,
        store = Store.Epic,
        label = { LocalStrings.current.storeEpic },
        painter = { painterResource(Bitmaps.Epic) },
    ),
    Ubisoft(
        priority = 101,
        store = Store.Ubisoft,
        label = { LocalStrings.current.storeUbisoft },
        painter = { painterResource(Bitmaps.Ubisoft) },
    ),
    Steam(
        priority = 102,
        store = Store.Steam,
        label = { LocalStrings.current.storeSteam },
        painter = { painterResource(Bitmaps.Steam) },
    ),
}