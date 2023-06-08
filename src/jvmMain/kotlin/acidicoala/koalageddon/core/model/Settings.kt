package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

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
                    langTag.startsWith("fr", ignoreCase = true) -> Language.French
                    langTag.startsWith("it", ignoreCase = true) -> Language.Italian
                    langTag.startsWith("pt", ignoreCase = true) -> Language.BrazilianPortuguese
                    langTag.startsWith("ru", ignoreCase = true) -> Language.Russian
                    langTag.startsWith("tr", ignoreCase = true) -> Language.Turkish
                    langTag.startsWith("zh", ignoreCase = true) -> Language.SimplifiedChinese
                    langTag.startsWith("pl", ignoreCase = true) -> Language.Polish
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

    // Languages are sorted according to their ISO 639-1 code

    @Serializable
    enum class Language(val locale: Locale) : ILangString {
        German(Locale("de")) {
            override fun text(strings: Strings) = strings.languageDe
        },
        English(Locale("en")) {
            override fun text(strings: Strings) = strings.languageEn
        },
        French(Locale("fr")) {
            override fun text(strings: Strings) = strings.languageFr
        },
        Italian(Locale("it")) {
            override fun text(strings: Strings) = strings.languageIt
        },
        BrazilianPortuguese(Locale("pt", "BR")) {
            override fun text(strings: Strings) = strings.languagePtBr
        },
        Russian(Locale("ru")) {
            override fun text(strings: Strings) = strings.languageRu
        },
        Turkish(Locale("tr")) {
            override fun text(strings: Strings) = strings.languageTr
        },
        SimplifiedChinese(Locale("zh", "CN")) {
            override fun text(strings: Strings) = strings.languageZhCn
        },
        Polish(Locale("pl")) {
            override fun text(strings: Strings) = strings.languagePl
        },
    }

    @Transient
    val strings = when (language) {
        Language.German -> Strings.German
        Language.English -> Strings.English
        Language.French -> Strings.French
        Language.Italian -> Strings.Italian
        Language.BrazilianPortuguese -> Strings.BrazilianPortuguese
        Language.Russian -> Strings.Russian
        Language.Turkish -> Strings.Turkish
        Language.SimplifiedChinese -> Strings.SimplifiedChinese
        Language.Polish -> Strings.Polish
    }
}
