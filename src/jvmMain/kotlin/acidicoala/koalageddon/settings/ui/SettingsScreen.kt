package acidicoala.koalageddon.settings.ui

import acidicoala.koalageddon.core.model.LangString
import acidicoala.koalageddon.core.model.Settings
import acidicoala.koalageddon.core.ui.composable.*
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SettingsScreen() {
    val screenModel: SettingsScreenModel by localDI().instance()
    val state by screenModel.collectAsState()

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
            DropdownOption(
                label = strings.theme,
                items = Settings.Theme.values(),
                selected = settings.theme,
                onSelect = screenModel::onThemeChanged,
                icon = when (settings.theme) {
                    Settings.Theme.Dark -> Icons.Default.DarkMode
                    Settings.Theme.Light -> Icons.Default.LightMode
                }
            )

            DropdownOption(
                label = strings.language,
                items = Settings.Language.values(),
                selected = settings.language,
                onSelect = screenModel::onLanguageChanged,
                icon = Icons.Default.Translate
            )

            SwitchOption(
                label = strings.downloadPreReleaseVersions,
                checked = settings.downloadPreReleaseVersions,
                onCheckedChange = screenModel::onDownloadPreReleaseVersionsChanged
            )

            Divider(Modifier.padding(vertical = 8.dp))

            ButtonOption(
                label = "",
                buttonLabel = strings.openDataDirectory,
                onClick = screenModel::onOpenDataDirectory
            )

            ButtonOption(
                label = LangString("%0" to state.cacheSize) { cacheSize }.text,
                buttonLabel = strings.clearCache,
                onClick = screenModel::onClearCache
            )

            Divider(Modifier.padding(vertical = 8.dp))

            ButtonOption(
                label = strings.version,
                buttonLabel = strings.checkForUpdates,
                onClick = screenModel::onCheckForUpdates
            )

            InfoOption(
                label = strings.buildTimestamp,
                info = state.formattedBuildTimestamp,
            )
        }
    }
}