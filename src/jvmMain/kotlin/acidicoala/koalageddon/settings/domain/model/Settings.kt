package acidicoala.koalageddon.settings.domain.model

import acidicoala.koalageddon.core.model.ITextString
import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Settings constructor(
    val theme: Theme = Theme.Dark,
    val language: Language = Language.English,
) {
    @Serializable
    enum class Theme : ITextString {
        Dark {
            override fun text(strings: Strings) = strings.themeDark
        },
        Light {
            override fun text(strings: Strings) = strings.themeLight
        }
    }

    @Serializable
    enum class Language : ITextString {
        English {
            override fun text(strings: Strings) = strings.languageEn
        },
        Russian {
            override fun text(strings: Strings) = strings.languageRu
        }
    }

    @Transient
    val strings = when (language) {
        Settings.Language.English -> Strings.English
        Settings.Language.Russian -> Strings.Russian
    }
}