package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.logging.logger
import acidicoala.koalageddon.di
import acidicoala.koalageddon.core.ui.theme.AppTheme
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.kodein.di.instance
import java.io.File

@Serializable
data class Settings(
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English,
) {
    enum class Theme : TextString {
        Dark {
            override fun factory(strings: Strings) = strings.themeDark
        },
        Light {
            override fun factory(strings: Strings) = strings.themeLight
        }
    }

    enum class Language : TextString {
        English {
            override fun factory(strings: Strings) = strings.languageEn
        },
        Russian {
            override fun factory(strings: Strings) = strings.languageRu
        }
    }

    val strings: Strings
        get() = when (language) {
            Language.English -> Strings.English
            Language.Russian -> Strings.Russian
        }

    val colors: Colors
        @Composable get() = when (theme) {
            Theme.Dark -> AppTheme.Material.darkColors
            Theme.Light -> AppTheme.Material.lightColors
        }

    val colorScheme: ColorScheme
        @Composable get() = when (theme) {
            Theme.Dark -> AppTheme.Material3.darkColorScheme
            Theme.Light -> AppTheme.Material3.lightColorScheme
        }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun update(function: (Settings) -> Settings) {
        withContext(Dispatchers.IO) {
            val settingsFlow: MutableStateFlow<Settings> by di.instance()
            val appPaths: AppPaths by di.instance()

            settingsFlow.update(function)

            val settings = settingsFlow.value

            // FIXME: Not working
            logger.debug("Saving app settings to: ${appPaths.settings}'")

            val settingsStream = appPaths.settings.toFile().outputStream()

            // TODO: Collect events instead
            // FIXME: Not working
            Json.encodeToStream(settings, settingsStream)
        }
    }
}