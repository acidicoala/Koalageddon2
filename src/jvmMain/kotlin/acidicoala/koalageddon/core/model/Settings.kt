package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.Locale

@Serializable
data class Settings constructor(
    val theme: Theme = Theme.Dark,
    val language: Language = systemLanguage,
    val downloadPreReleaseVersions: Boolean = false,
) {
    companion object {
        val systemLanguage: Language
            get() {
                val langTag = Locale.getDefault().toLanguageTag()

                return when {
                    langTag.startsWith("de", ignoreCase = true) -> Language.German
                    langTag.startsWith("it", ignoreCase = true) -> Language.Italian
                    langTag.startsWith("ru", ignoreCase = true) -> Language.Russian
                    langTag.startsWith("pt", ignoreCase = true) -> Language.Brazilian_Portuguese
                    langTag.startsWith("pt", ignoreCase = true) -> Language.Turkish
                    else -> Language.English
                }
            }
    }

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
    enum class Language(val locale: Locale) : ILangString {
        English(Locale.ENGLISH) {
            override fun text(strings: Strings) = strings.languageEn
        },
        German(Locale.GERMAN) {
            override fun text(strings: Strings) = strings.languageDe
        },
        Italian(Locale.ITALIAN) {
            override fun text(strings: Strings) = strings.languageIt
        },
        Russian(Locale("ru")) {
            override fun text(strings: Strings) = strings.languageRu
        },
        Brazilian_Portuguese(Locale("pt")) {
            override fun text(strings: Strings) = strings.languagePt_BR
        },
        Turkish(Locale("tr")) {
            override fun text(strings: Strings) = strings.languageTr
        }
    }

    @Transient
    val strings = when (language) {
        Language.English -> Strings.English
        Language.German -> Strings.German
        Language.Italian -> Strings.Italian
        Language.Russian -> Strings.Russian
        Language.Brazilian_Portuguese -> Strings.Brazilian_Portuguese
        Language.Brazilian_Portuguese -> Strings.Turkish
    }
}
