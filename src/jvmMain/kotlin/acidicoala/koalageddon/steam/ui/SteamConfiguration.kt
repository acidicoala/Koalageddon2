package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI
import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI.Config
import acidicoala.koalageddon.core.ui.composable.*
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SteamConfiguration(
    config: Config,
    screenModel: SteamScreenModel,
) {
    val state by screenModel.collectAsState()
    val strings = LocalStrings.current
    var resetKey by remember { mutableStateOf(0) }

    Column {
        Box {
            TooltipArea(
                tooltip = {
                    Card(elevation = 8.dp) {
                        Text(
                            text = strings.reloadConfigTooltip,
                            modifier = Modifier.padding(DefaultContentPadding)
                        )
                    }
                },
            ) {
                ButtonOption(
                    label = "",
                    enabled = state.isSteamRunning,
                    buttonIcon = Icons.Default.Sync,
                    buttonLabel = strings.reloadConfig,
                    onClick = screenModel::onReloadConfig
                )
            }
        }

        SwitchOption(
            label = strings.logging,
            checked = config.logging,
            onCheckedChange = { screenModel.onConfigChange(config.copy(logging = it)) }
        )

        SwitchOption(
            label = strings.unlockFamilySharing,
            checked = config.unlockFamilySharing,
            onCheckedChange = { screenModel.onConfigChange(config.copy(unlockFamilySharing = it)) }
        )

        DropdownOption(
            label = strings.defaultAppStatus,
            items = SmokeAPI.AppStatus.validAppStatuses,
            selected = config.defaultAppStatus,
            onSelect = { screenModel.onConfigChange(config.copy(defaultAppStatus = it)) }
        )

        IntMapDropdownOption(
            resetKey = resetKey,
            label = strings.overrideAppStatus,
            keyLabel = strings.appId,
            map = config.overrideAppStatus,
            defaultValue = SmokeAPI.AppStatus.Original,
            validValues = SmokeAPI.AppStatus.validAppStatuses,
            onMapChange = { screenModel.onConfigChange(config.copy(overrideAppStatus = it)) }
        )

        IntMapDropdownOption(
            resetKey = resetKey,
            label = strings.overrideDlcStatus,
            keyLabel = strings.dlcId,
            map = config.overrideDlcStatus,
            defaultValue = SmokeAPI.AppStatus.Original,
            validValues = SmokeAPI.AppStatus.validDlcStatuses,
            onMapChange = { screenModel.onConfigChange(config.copy(overrideDlcStatus = it)) }
        )

        SwitchOption(
            label = strings.autoInjectInventory,
            checked = config.autoInjectInventory,
            onCheckedChange = { screenModel.onConfigChange(config.copy(autoInjectInventory = it)) }
        )

        IntListOption(
            resetKey = resetKey,
            label = strings.extraInventoryItems,
            itemLabel = strings.itemId,
            list = config.extraInventoryItems,
            onListChange = { screenModel.onConfigChange(config.copy(extraInventoryItems = it)) }
        )

        ButtonOption(
            label = "",
            buttonIcon = Icons.Default.Restore,
            outlined = true,
            buttonLabel = strings.restoreDefaultConfiguration,
            onClick = {
                screenModel.onConfigChange(Config())
                resetKey++
            }
        )
    }
}