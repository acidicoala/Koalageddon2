package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Settings constructor(
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English, // TODO: Auto-detect system lang
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
}
