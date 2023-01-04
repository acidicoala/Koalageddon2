package acidicoala.koalageddon.feature.settings.ui

import acidicoala.koalageddon.core.use_case.ShowSnackbar
import acidicoala.koalageddon.feature.settings.domain.model.Settings
import acidicoala.koalageddon.feature.settings.domain.use_case.SaveSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SettingsScreenModel(override val di: DI) : DIAware {
    private val scope: CoroutineScope by instance()
    private val showSnackbar: ShowSnackbar by instance()
    private val saveSettings: SaveSettings by instance()

    fun onThemeChanged(theme: Settings.Theme) {
        saveSettings { copy(theme = theme) }
    }

    fun onLanguageChanged(language: Settings.Language) {
        saveSettings { copy(language = language) }
    }

    fun onCheckForUpdates() {
        scope.launch {
            showSnackbar("NOT IMPLEMENTED")
        }
    }
}