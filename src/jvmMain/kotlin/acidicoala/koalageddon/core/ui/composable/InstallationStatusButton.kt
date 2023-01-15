package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.InstallationChecklist
import acidicoala.koalageddon.core.model.InstallationStatus
import acidicoala.koalageddon.core.model.OpenState
import acidicoala.koalageddon.core.model.Store
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InstallationStatusOption(checklist: InstallationChecklist?, store: Store) {
    val strings = LocalStrings.current
    val dialogOpenState = remember { OpenState() }
    val installationStatus = checklist?.installationStatus ?: InstallationStatus.Updating

    ControlOption(strings.installationStatus) {
        OutlinedButton(onClick = dialogOpenState::open) {
            Icon(
                contentDescription = null,
                imageVector = installationStatus.icon,
                tint = installationStatus.color(),
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = installationStatus.label.text
            )

            if (checklist != null) {
                InstallationChecklistDropdown(
                    openState = dialogOpenState,
                    checklist = checklist,
                    store = store
                )
            }
        }
    }
}