package acidicoala.koalageddon.settings.ui

import acidicoala.koalageddon.core.ui.composable.ButtonOption
import acidicoala.koalageddon.core.ui.composable.DropdownOption
import acidicoala.koalageddon.core.ui.composable.VerticalScrollContainer
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import acidicoala.koalageddon.core.model.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SettingsScreen() {
    val screenModel: SettingsScreenModel by localDI().instance()

    val settings = LocalSettings.current
    val strings = LocalStrings.current

    VerticalScrollContainer(
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = DefaultMaxWidth)
                .padding(DefaultContentPadding),
        ) {
            ButtonOption(
                label = strings.version,
                buttonLabel = strings.checkForUpdates,
                onClick = screenModel::onCheckForUpdates
            )

            DropdownOption(
                label = strings.theme,
                items = Settings.Theme.values(),
                selected = settings.theme,
                onSelect = screenModel::onThemeChanged
            )

            DropdownOption(
                label = strings.language,
                items = Settings.Language.values(),
                selected = settings.language,
                onSelect = screenModel::onLanguageChanged
            )

            Divider(Modifier.padding(vertical = 8.dp))

            ButtonOption(
                label = "",
                buttonLabel = strings.openDataDirectory,
                onClick = screenModel::onOpenDataDirectory
            )

            ButtonOption(
                label = "Cache size: xyz MB", // TODO
                buttonLabel = strings.clearCache,
                onClick = screenModel::onClearCache
            )
        }
    }
}