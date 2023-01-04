package acidicoala.koalageddon.feature.settings.di

import acidicoala.koalageddon.core.use_case.ShowSnackbar
import acidicoala.koalageddon.feature.settings.domain.use_case.ReadSettings
import acidicoala.koalageddon.feature.settings.domain.use_case.SaveSettings
import acidicoala.koalageddon.feature.settings.ui.SettingsScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.*

val settingsModule = DI.Module(name = "Settings") {
    bindProvider { SaveSettings(di) }
    bindProvider { ShowSnackbar(di) }
    bindProvider { SettingsScreenModel(di) }

    bindSingleton {
        val readSettings = ReadSettings(di)

        MutableStateFlow(readSettings())
    }
}