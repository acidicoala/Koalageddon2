package acidicoala.koalageddon.settings.di

import acidicoala.koalageddon.settings.domain.use_case.ReadSettings
import acidicoala.koalageddon.settings.domain.use_case.SaveSettings
import acidicoala.koalageddon.settings.ui.SettingsScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val settingsModule = DI.Module(name = "Settings") {
    bindProvider { SaveSettings(di) }
    bindSingleton { MutableStateFlow(ReadSettings(di)()) }
    bindSingleton { SettingsScreenModel(di) }
}
