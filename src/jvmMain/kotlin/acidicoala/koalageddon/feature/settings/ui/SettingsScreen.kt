package acidicoala.koalageddon.feature.settings.ui

import acidicoala.koalageddon.core.ui.composable.DefaultSpacer
import acidicoala.koalageddon.core.ui.composable.DropdownOption
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.feature.settings.domain.model.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.widthIn(max = 320.dp).padding(DefaultContentPadding)
        ) {
            DropdownOption(
                label = strings.theme,
                items = Settings.Theme.values(),
                selected = settings.theme,
                onSelect = screenModel::onThemeChanged
            )

            DefaultSpacer()

            DropdownOption(
                label = strings.language,
                items = Settings.Language.values(),
                selected = settings.language,
                onSelect = screenModel::onLanguageChanged
            )

            Button(onClick = screenModel::onCheckForUpdates) {
                Text(text = strings.checkForUpdates)
            }

//        var bool by remember { mutableStateOf(false) }
//        SwitchOption("Test setting", bool) { bool = it }
        }
    }
}