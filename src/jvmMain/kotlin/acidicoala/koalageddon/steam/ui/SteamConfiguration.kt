package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI
import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI.Config
import acidicoala.koalageddon.core.ui.composable.DropdownOption
import acidicoala.koalageddon.core.ui.composable.IntListOption
import acidicoala.koalageddon.core.ui.composable.IntMapDropdownOption
import acidicoala.koalageddon.core.ui.composable.SwitchOption
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun SteamConfiguration(config: Config, onConfigChange: (Config) -> Unit) {
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

        IntMapDropdownOption(
            label = strings.overrideAppStatus,
            keyLabel = strings.appId,
            map = config.overrideAppStatus,
            defaultValue = SmokeAPI.AppStatus.Original,
            validValues = SmokeAPI.AppStatus.validAppStatuses,
            onMapChange = { onConfigChange(config.copy(overrideAppStatus = it)) }
        )

        IntMapDropdownOption(
            label = strings.overrideDlcStatus,
            keyLabel = strings.dlcId,
            map = config.overrideDlcStatus,
            defaultValue = SmokeAPI.AppStatus.Original,
            validValues = SmokeAPI.AppStatus.validDlcStatuses,
            onMapChange = { onConfigChange(config.copy(overrideDlcStatus = it)) }
        )

        SwitchOption(
            label = strings.autoInjectInventory,
            checked = config.autoInjectInventory,
            onCheckedChange = { onConfigChange(config.copy(autoInjectInventory = it)) }
        )

        IntListOption(
            label = strings.extraInventoryItems,
            itemLabel = strings.itemId,
            list = config.extraInventoryItems,
            onListChange = { onConfigChange(config.copy(extraInventoryItems = it)) }
        )

    }
}