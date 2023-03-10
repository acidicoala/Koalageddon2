package acidicoala.koalageddon.settings.di

import acidicoala.koalageddon.settings.domain.use_case.ClearCache
import acidicoala.koalageddon.settings.domain.use_case.GetCacheSize
import acidicoala.koalageddon.core.use_case.SaveSettings
import acidicoala.koalageddon.settings.ui.SettingsScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val settingsModule = DI.Module(name = "Settings") {
    bindProvider { SaveSettings(di) }
    bindSingleton { SettingsScreenModel(di) }

    bindProvider { ClearCache(di) }
    bindProvider { GetCacheSize(di) }
}
