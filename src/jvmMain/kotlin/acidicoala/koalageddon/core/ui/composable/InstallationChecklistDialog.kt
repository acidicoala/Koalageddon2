package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.InstallationChecklist
import acidicoala.koalageddon.core.model.InstallationStatus
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class ChecklistOpenState {
    private var _open = mutableStateOf(false)
    val open: State<Boolean> = _open

    fun onOpenChanged(newOpen: Boolean) {
        _open.value = newOpen
    }
}

@Composable
fun rememberChecklistOpenState() = remember { ChecklistOpenState() }

@Composable
fun InstallationChecklistDropdown(openState: ChecklistOpenState, checklist: InstallationChecklist) {
    if (!openState.open.value) {
        return
    }

    val strings = LocalStrings.current

    DropdownMenu(
        expanded = openState.open.value,
        onDismissRequest = { openState.onOpenChanged(false) },
    ) {
        @Composable
        fun ListItem(checked: Boolean?, label: String) {
            Row(Modifier.padding(vertical = 4.dp)) {
                Box(Modifier.size(16.dp)) {
                    if (checked != null) {
                        Icon(
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            imageVector = InstallationStatus.fromBoolean(checked).icon,
                            tint = InstallationStatus.fromBoolean(checked).color()
                        )
                    }
                }

                Spacer(Modifier.size(8.dp))

                Text(text = label)
            }
        }

        Column(Modifier.padding(horizontal = 8.dp)) {
            ListItem(
                checked = checklist.loaderDll,
                label = strings.loaderDll
            )

            ListItem(
                checked = checklist.loaderConfig,
                label = strings.loaderConfig
            )

            ListItem(
                checked = checklist.unlockerDll,
                label = strings.unlockerDll
            )

            ListItem(
                checked = checklist.unlockerConfig,
                label = strings.unlockerConfig
            )
        }
    }
}