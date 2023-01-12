package acidicoala.koalageddon.core.values

import acidicoala.koalageddon.BuildConfig

sealed class Strings(
    val checkForUpdates: String,
    val language: String,
    val languageEn: String = "English",
    val languageRu: String = "Русский",
    val openDataDirectory: String,
    val reloadConfig: String,
    val reloadConfigSuccess: String,
    val reloadConfigError: String,
    val settings: String,
    val storeSteam: String = "Steam",
    val theme: String,
    val themeDark: String,
    val themeLight: String,
    val version: String,
) {
    object English : Strings(
        checkForUpdates = "Check for updates",
        language = "Language",
        openDataDirectory = "Open data directory",
        reloadConfig = "Reload configuration",
        reloadConfigSuccess = "✅ Configuration was successfully reloaded",
        reloadConfigError = "❌ Error reloading configuration",
        settings = "Settings",
        theme = "Theme",
        themeDark = "Dark",
        themeLight = "Light",
        version = "Version v${BuildConfig.APP_VERSION}",
    )

    object Russian : Strings(
        checkForUpdates = "Проверить обновления",
        language = "Язык",
        openDataDirectory = "Открыть директорию данных",
        reloadConfig = "Перезагрузить конфигурацию",
        reloadConfigSuccess = "✅ Конфигурация была успешно перезагружена",
        reloadConfigError = "❌ Ошибка при перезагруки конфигурации",
        settings = "Настройки",
        theme = "Тема",
        themeDark = "Тёмная",
        themeLight = "Светлая",
        version = "Версия v${BuildConfig.APP_VERSION}",
    )
}


