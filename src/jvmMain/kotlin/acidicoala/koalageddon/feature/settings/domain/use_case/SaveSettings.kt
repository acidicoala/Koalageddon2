package acidicoala.koalageddon.feature.settings.domain.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.serialization.json
import acidicoala.koalageddon.feature.settings.domain.model.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SaveSettings(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()
    private val logger: AppLogger by instance()
    private val settingsFlow: MutableStateFlow<Settings> by instance()

    @OptIn(ExperimentalSerializationApi::class)
    operator fun invoke(function: Settings.() -> Settings): Boolean = try {
        val settings = settingsFlow.value

        val newSettings = function(settings)

        if (settings != newSettings) {
            settingsFlow.update { newSettings }

            val path = paths.settings

            logger.debug("""Saving app settings to "$path"""")

            val settingsStream = path.toFile().outputStream()

            json.encodeToStream(newSettings, settingsStream)
        }

        true
    } catch (e: Exception) {
        logger.error(e, "Failed to update settings")

        false
    }
}