package acidicoala.koalageddon.settings.ui

import acidicoala.koalageddon.core.ui.composable.ButtonOption
import acidicoala.koalageddon.core.ui.composable.DropdownOption
import acidicoala.koalageddon.core.ui.composable.VerticalScrollContainer
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.settings.domain.model.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SettingsScreen() {
    val settings = LocalSettings.current
    val strings = LocalStrings.current

    val screenModel: SettingsScreenModel by localDI().instance()

    VerticalScrollContainer(
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 480.dp).padding(DefaultContentPadding),
        ) {
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

            ButtonOption(
                label = strings.version,
                buttonLabel = strings.checkForUpdates,
                onClick = screenModel::onCheckForUpdates
            )
        }
    }
}