package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.*
import acidicoala.koalageddon.core.ui.composable.*
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.AppTheme
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SteamStoreScreen() {
    val screenModel: SteamScreenModel by localDI().instance()
    val state by screenModel.collectAsState()
    val strings = LocalStrings.current

    val installConfirmationDialogState = remember { OpenState() }

    LaunchedEffect(screenModel) {
        screenModel.onRefreshState()
    }

    VerticalScrollContainer(
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.widthIn(max = DefaultMaxWidth).padding(DefaultContentPadding),
        ) {
            SectionLabel(
                icon = Icons.Default.Info, label = strings.information
            )

            RunningStatusOption(
                label = LangString("%0" to Store.Steam.executable) { storeProcessStatus }.text,
                running = state.isSteamRunning
            )

            ButtonOption(
                label = strings.unlocker,
                buttonIcon = Icons.Default.OpenInNew,
                buttonLabel = KoalaTool.SmokeAPI.name,
                outlined = true,
                onClick = screenModel::onUnlockerClick
            )

            ButtonOption(
                label = "",
                buttonIcon = Icons.Default.Summarize,
                buttonLabel = strings.openLogFile,
                enabled = state.logFileExists,
                outlined = true,
                onClick = screenModel::onOpenLogs
            )

            Divider(Modifier.padding(vertical = 8.dp))

            SectionLabel(
                icon = Icons.Default.InstallDesktop, label = strings.installation
            )

            ButtonOption(
                label = "",
                buttonIcon = Icons.Default.Refresh,
                buttonLabel = strings.refreshStatus,
                outlined = true,
                onClick = screenModel::onRefreshState
            )

            InstallationStatusOption(
                checklist = state.installationChecklist,
                store = Store.Steam
            )

            ButtonOption(
                enabled = state.installProgressMessage == null && state.installationChecklist != null,
                label = strings.modifyInstallation,
                buttonIcon = when (state.installationChecklist?.installationStatus) {
                    null -> InstallationStatus.Updating.icon
                    is InstallationStatus.Installed -> Icons.Default.Delete
                    else -> Icons.Default.InstallDesktop
                },
                buttonLabel = when (state.installationChecklist?.installationStatus) {
                    null -> strings.computing
                    is InstallationStatus.Installed -> strings.remove
                    else -> strings.install
                },
                onClick = {
                    if (state.isSteamRunning) {
                        installConfirmationDialogState.open()
                    } else {
                        screenModel.onModifyInstallation()
                    }
                }
            )

            state.installProgressMessage?.let { message ->
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = AppTheme.AppColors.Orange.copy(alpha = .25f),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            state.config?.let { config ->
                Divider(Modifier.padding(vertical = 8.dp))

                SectionLabel(
                    icon = Icons.Default.SettingsApplications, label = strings.configuration
                )

                SteamConfiguration(
                    config = config,
                    screenModel = screenModel,
                )
            }
        }
    }

    ConfirmationDialog(
        openState = installConfirmationDialogState,
        title = strings.confirmForceModifyInstallationTitle,
        message = LangString("%0" to Store.Steam.executable) {
            confirmForceModifyInstallationMessage
        }.text,
        onOk = screenModel::onModifyInstallation
    )
}
