package acidicoala.koalageddon

import acidicoala.koalageddon.core.di.coreModule
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.AppTheme
import acidicoala.koalageddon.core.values.Strings
import acidicoala.koalageddon.settings.di.settingsModule
import acidicoala.koalageddon.settings.domain.model.Settings
import acidicoala.koalageddon.home.ui.HomeScreen
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.bindInstance
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import java.awt.Dimension

fun main() = singleWindowApplication {
    window.minimumSize = Dimension(640, 480)

    val appScope = rememberCoroutineScope()

    withDI({
        bindInstance { appScope }
        importAll(coreModule, settingsModule)
    }) {
        val settingsFlow: MutableStateFlow<Settings> by localDI().instance()
        val settings by settingsFlow.collectAsState()

        val strings = when (settings.language) {
            Settings.Language.English -> Strings.English
            Settings.Language.Russian -> Strings.Russian
        }

        val colors = when (settings.theme) {
            Settings.Theme.Dark -> AppTheme.Material.darkColors
            Settings.Theme.Light -> AppTheme.Material.lightColors
        }

        MaterialTheme(colors = colors) {
            CompositionLocalProvider(LocalSettings provides settings) {
                CompositionLocalProvider(LocalStrings provides strings) {
//                    CompositionLocalProvider(LocalElevationOverlay provides null){
                        HomeScreen()
//                    }
                }
            }
        }
    }
}
