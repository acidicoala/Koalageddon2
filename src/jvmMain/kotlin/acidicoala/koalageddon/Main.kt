package acidicoala.koalageddon

import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.model.Settings
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.main.ui.MainScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import java.awt.Dimension

val di = DI {
    bindSingleton { MutableStateFlow(Settings()) }
    bindSingleton { AppPaths() }
}

fun main() = singleWindowApplication {
    window.minimumSize = Dimension(640, 480)

    val settingsFlow: MutableStateFlow<Settings> by di.instance()

    val settings by settingsFlow.collectAsState()

    // TODO: Some components, like the DropdownMenu are not yet available in material3
    androidx.compose.material.MaterialTheme(colors = settings.colors) {
        MaterialTheme(colorScheme = settings.colorScheme) {
            CompositionLocalProvider(LocalSettings provides settings) {
                CompositionLocalProvider(LocalStrings provides settings.strings) {
                    MainScreen()
                }
            }
        }
    }
}
