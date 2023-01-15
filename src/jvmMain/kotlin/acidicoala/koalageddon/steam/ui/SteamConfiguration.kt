package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI
import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI.Config
import acidicoala.koalageddon.core.ui.composable.DropdownOption
import acidicoala.koalageddon.core.ui.composable.MapDropdownOption
import acidicoala.koalageddon.core.ui.composable.SwitchOption
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SmokeApiConfiguration(config: Config, onConfigChange: (Config) -> Unit) {
    val strings = LocalStrings.current

    Column {
        SwitchOption(
            label = strings.logging,
            checked = config.logging,
            onCheckedChange = { onConfigChange(config.copy(logging = it)) }
        )

        SwitchOption(
            label = strings.unlockFamilySharing,
            checked = config.unlockFamilySharing,
            onCheckedChange = { onConfigChange(config.copy(unlockFamilySharing = it)) }
        )

        DropdownOption(
            label = strings.defaultAppStatus,
            items = SmokeAPI.AppStatus.validAppStatuses,
            selected = config.defaultAppStatus,
            onSelect = { onConfigChange(config.copy(defaultAppStatus = it)) }
        )

        MapDropdownOption(
            label = strings.overrideAppStatus,
            keyLabel = strings.appId,
            map = config.overrideAppStatus,
            defaultEntry = "0" to SmokeAPI.AppStatus.Original,
            validValues = SmokeAPI.AppStatus.validAppStatuses,
            keyboardType = KeyboardType.Number,
            keyMapper = { if (it.isBlank()) "0" else it.toLongOrNull()?.toString() },
            onMapChange = { onConfigChange(config.copy(overrideAppStatus = it)) }
        )
    }
}