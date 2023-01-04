package acidicoala.koalageddon

import acidicoala.koalageddon.core.di.coreModule
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.feature.settings.di.settingsModule
import acidicoala.koalageddon.feature.settings.domain.model.Settings
import acidicoala.koalageddon.feature.settings.domain.model.colorScheme
import acidicoala.koalageddon.feature.settings.domain.model.colors
import acidicoala.koalageddon.feature.settings.domain.model.strings
import acidicoala.koalageddon.home.ui.HomeScreen
import androidx.compose.material3.MaterialTheme
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

        // TODO: Some components, like the DropdownMenu are not yet available in
        //       material3 version bundled with desktop compose. Once they are available,
        //       they need to be migrated and legacy theme needs to be removed.
        androidx.compose.material.MaterialTheme(colors = settings.colors) {
            MaterialTheme(colorScheme = settings.colorScheme) {
                CompositionLocalProvider(LocalSettings provides settings) {
                    CompositionLocalProvider(LocalStrings provides settings.strings) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}
