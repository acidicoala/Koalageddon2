package acidicoala.koalageddon.core.ui.composition

import acidicoala.koalageddon.settings.domain.model.Settings
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.runtime.compositionLocalOf

val LocalStrings = compositionLocalOf<Strings> { Strings.English }

val LocalSettings = compositionLocalOf<Settings> {
    throw IllegalStateException("Composition [LocalSettings] not found")
}
