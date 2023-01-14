package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Bitmaps
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Settings constructor(
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English,
    val lastHomeTab: HomeTab = HomeTab.Settings,
) {
    @Serializable
    enum class Theme : ILangString {
        Dark {
            override fun text(strings: Strings) = strings.themeDark
        },
        Light {
            override fun text(strings: Strings) = strings.themeLight
        }
    }

    @Serializable
    enum class Language : ILangString {
        English {
            override fun text(strings: Strings) = strings.languageEn
        },
        Russian {
            override fun text(strings: Strings) = strings.languageRu
        }
    }

    @Transient
    val strings = when (language) {
        Language.English -> Strings.English
        Language.Russian -> Strings.Russian
    }

    @Serializable
    enum class HomeTab(
        val label: @Composable () -> String,
        val painter: @Composable () -> Painter
    ) {
        Settings(
            label = { LocalStrings.current.settings },
            painter = { painterResource(Bitmaps.Settings) },
        ),

        Steam(
            label = { LocalStrings.current.storeSteam },
            painter = { painterResource(Bitmaps.Steam) },
        );
    }
}