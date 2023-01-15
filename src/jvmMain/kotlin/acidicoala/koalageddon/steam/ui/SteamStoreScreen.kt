package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.InstallationStatus
import acidicoala.koalageddon.core.model.Store
import acidicoala.koalageddon.core.ui.composable.*
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.AppTheme
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InstallDesktop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                onClick = screenModel::onModifyInstallation
            )

            state.installProgressMessage?.let { message ->
                Surface(
                    shape = RoundedCornerShape(4.dp),
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

            Divider(Modifier.padding(vertical = 8.dp))

            SectionLabel(
                icon = Icons.Default.SettingsApplications, label = strings.configuration
            )

            // TODO: Enabled only if steam is running and SmokeAPI installed
            ButtonOption(
                label = "",
                enabled = true,
                buttonIcon = Icons.Default.Refresh,
                buttonLabel = strings.reloadConfig,
                onClick = screenModel::onReloadConfig
            )

            state.config?.let { config ->
                SmokeApiConfiguration(
                    config = config,
                    onConfigChange = screenModel::onConfigChange,
                )
            }
        }
    }
}
