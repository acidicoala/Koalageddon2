package acidicoala.koalageddon.settings.ui

import acidicoala.koalageddon.BuildConfig
import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.model.LangString
import acidicoala.koalageddon.core.model.Settings
import acidicoala.koalageddon.core.use_case.*
import acidicoala.koalageddon.settings.domain.use_case.ClearCache
import acidicoala.koalageddon.settings.domain.use_case.GetCacheSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SettingsScreenModel(
    override val di: DI,
    private val stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
) : DIAware, StateFlow<SettingsScreenModel.State> by stateFlow {
    data class State(
        val cacheSize: String = "",
        val formattedBuildTimestamp: String = "",
    )

    private val logger: AppLogger by instance()
    private val scope: CoroutineScope by instance()
    private val showSnackbar: ShowSnackbar by instance()
    private val appPaths: AppPaths by instance()
    private val saveSettings: SaveSettings by instance()
    private val openResourceLink: OpenResourceLink by instance()
    private val getCacheSize: GetCacheSize by instance()
    private val clearCache: ClearCache by instance()
    private val getHumanReadableSize: GetHumanReadableSize by instance()
    private val getFormattedTimestamp: GetFormattedTimestamp by instance()
    private val settingsFlow: MutableStateFlow<Settings> by instance()

    init {
        scope.launch {
            settingsFlow.collectLatest {
                onRefreshStatus()
            }
        }
    }

    private fun onRefreshStatus() {
        stateFlow.update {
            it.copy(
                cacheSize = getHumanReadableSize(getCacheSize()),
                formattedBuildTimestamp = getFormattedTimestamp(
                    BuildConfig.BUILD_TIME,
                    settingsFlow.value.language.locale
                )
            )
        }
    }

    fun onThemeChanged(theme: Settings.Theme) {
        logger.info("Setting theme to $theme")

        saveSettings { copy(theme = theme) }
    }

    fun onLanguageChanged(language: Settings.Language) {
        logger.info("Settings language to $language")

        saveSettings { copy(language = language) }
    }

    fun onDownloadPreReleaseVersionsChanged(download: Boolean) {
        logger.info("Settings download pre-release versions to $download")

        saveSettings { copy(downloadPreReleaseVersions = download) }
    }

    fun onCheckForUpdates() {
        scope.launch {
            showSnackbar(LangString { inDevelopment }) // TODO: Implement
        }
    }

    fun onOpenDataDirectory() {
        openResourceLink(appPaths.dataDir)
    }

    fun onClearCache() {
        scope.launch {
            clearCache()
            onRefreshStatus()
        }
    }
}