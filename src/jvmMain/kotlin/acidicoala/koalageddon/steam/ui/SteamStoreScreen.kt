package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.InstallationStatus
import acidicoala.koalageddon.core.ui.composable.*
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.InstallDesktop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SteamStoreScreen() {
    val screenModel: SteamScreenModel by localDI().instance()
    val state by screenModel.collectAsState()
    val strings = LocalStrings.current

    VerticalScrollContainer(
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = DefaultMaxWidth)
                .padding(DefaultContentPadding),
        ) {
            SectionLabel(
                icon = Icons.Default.InstallDesktop,
                label = strings.installation
            )

            InstallationStatusOption(
                checklist = state.installationChecklist
            )

            ButtonOption(
                label = strings.modifyInstallation,
                buttonIcon = when (state.installationChecklist.installationStatus) {
                    InstallationStatus.Installed -> Icons.Default.Delete
                    else -> Icons.Default.InstallDesktop
                },
                buttonLabel = when (state.installationChecklist.installationStatus) {
                    InstallationStatus.Installed -> strings.uninstall
                    else -> strings.install
                },
                onClick = screenModel::onModifyInstallation
            )

            SectionLabel(
                icon = Icons.Default.SettingsApplications,
                label = strings.configuration
            )

            ButtonOption(
                label = "",
                buttonIcon = Icons.Default.Refresh,
                buttonLabel = strings.reloadConfig,
                onClick = screenModel::onReloadConfig
            )
        }
    }
}
