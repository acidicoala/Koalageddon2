package acidicoala.koalageddon.feature.settings.domain.model

import acidicoala.koalageddon.core.model.TextString
import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.Serializable

@Serializable
data class Settings constructor(
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English,
) {
    @Serializable
    enum class Theme : TextString {
        Dark {
            override fun factory(strings: Strings) = strings.themeDark
        },
        Light {
            override fun factory(strings: Strings) = strings.themeLight
        }
    }

    @Serializable
    enum class Language : TextString {
        English {
            override fun factory(strings: Strings) = strings.languageEn
        },
        Russian {
            override fun factory(strings: Strings) = strings.languageRu
        }
    }
}