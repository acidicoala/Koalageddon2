package acidicoala.koalageddon.settings.domain.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.io.appJson
import acidicoala.koalageddon.core.model.Settings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.io.path.exists

class ReadSettings(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()
    private val logger: AppLogger by instance()

    @OptIn(ExperimentalSerializationApi::class)
    operator fun invoke(): Settings = try {
        val path = paths.settings

        if (path.exists()) {
            logger.info("""Reading settings from "$path"""")

            val settingsStream = path.toFile().inputStream()

            appJson.decodeFromStream(settingsStream)
        } else {
            logger.info("No settings found on disk. Using default settings")

            Settings()
        }
    } catch (e: Exception) {
        logger.error(e, "Failed to read settings from disk. Using default settings.")

        Settings()
    }
}