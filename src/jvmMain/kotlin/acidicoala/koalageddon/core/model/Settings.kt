package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Settings constructor(
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English,
) {
    @Serializable
    enum class Theme : ILangString {
        Dark {
            override fun Strings.text() = themeDark
        },
        Light {
            override fun Strings.text() = themeLight
        }
    }

    @Serializable
    enum class Language : ILangString {
        English {
            override fun Strings.text() = languageEn
        },
        Russian {
            override fun Strings.text() = languageRu
        }
    }

    @Transient
    val strings = when (language) {
        Language.English -> Strings.English
        Language.Russian -> Strings.Russian
    }
}
