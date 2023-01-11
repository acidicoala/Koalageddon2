package acidicoala.koalageddon.settings.ui

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.TextString
import acidicoala.koalageddon.core.use_case.ShowSnackbar
import acidicoala.koalageddon.settings.domain.model.Settings
import acidicoala.koalageddon.settings.domain.use_case.SaveSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SettingsScreenModel(override val di: DI) : DIAware {
    private val logger: AppLogger by instance()
    private val scope: CoroutineScope by instance()
    private val showSnackbar: ShowSnackbar by instance()
    private val saveSettings: SaveSettings by instance()

    fun onThemeChanged(theme: Settings.Theme) {
        logger.info("Setting theme to $theme")

        saveSettings { copy(theme = theme) }
    }

    fun onLanguageChanged(language: Settings.Language) {
        logger.info("Settings language to $language")

        saveSettings { copy(language = language) }
    }

    fun onCheckForUpdates() {
        scope.launch {
            // TODO
            showSnackbar(TextString { "NOT IMPLEMENTED" })
        }
    }
}