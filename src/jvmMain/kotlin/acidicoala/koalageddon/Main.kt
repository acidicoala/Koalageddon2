package acidicoala.koalageddon

import acidicoala.koalageddon.core.di.coreModule
import acidicoala.koalageddon.core.model.Settings
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.AppRippleTheme
import acidicoala.koalageddon.core.ui.theme.AppTheme
import acidicoala.koalageddon.core.values.Bitmaps
import acidicoala.koalageddon.home.ui.HomeScreen
import acidicoala.koalageddon.settings.di.settingsModule
import acidicoala.koalageddon.steam.di.steamModule
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.bindInstance
import org.kodein.di.compose.localDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance
import java.awt.Dimension
import javax.swing.UIManager

fun main() = application {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource(Bitmaps.Icon),
        title = "Koalageddon"
    ) {
        window.minimumSize = Dimension(720, 480)

        val appScope = rememberCoroutineScope()

        withDI({
            bindInstance { appScope }
            importAll(coreModule, settingsModule, steamModule)
        }) {
            val settingsFlow: MutableStateFlow<Settings> by localDI().instance()
            val settings by settingsFlow.collectAsState()

            val colors = when (settings.theme) {
                Settings.Theme.Dark -> AppTheme.Material.darkColors
                Settings.Theme.Light -> AppTheme.Material.lightColors
            }

            // CompositionLocalProvider(LocalElevationOverlay provides null){

            MaterialTheme(colors = colors, shapes = AppTheme.shapes) {
                CompositionLocalProvider(LocalSettings provides settings) {
                    CompositionLocalProvider(LocalStrings provides settings.strings) {
                        CompositionLocalProvider(LocalRippleTheme provides AppRippleTheme) {
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
