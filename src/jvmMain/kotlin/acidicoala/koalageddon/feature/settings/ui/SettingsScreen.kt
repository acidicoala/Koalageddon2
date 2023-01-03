package acidicoala.koalageddon.feature.settings.ui

import acidicoala.koalageddon.core.model.Settings
import acidicoala.koalageddon.core.ui.composable.DefaultSpacer
import acidicoala.koalageddon.core.ui.composable.DropdownOption
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val settings = LocalSettings.current
    val strings = LocalStrings.current

    val scope = rememberCoroutineScope()

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
                onSelect = { theme ->
                    scope.launch {
                        settings.update { it.copy(theme = theme) }
                    }
                }
            )

            DefaultSpacer()

            DropdownOption(
                label = strings.language,
                items = Settings.Language.values(),
                selected = settings.language,
                onSelect = { language ->
                    scope.launch {
                        settings.update { it.copy(language = language) }
                    }
                }
            )

//        var bool by remember { mutableStateOf(false) }
//        SwitchOption("Test setting", bool) { bool = it }
        }
    }
}