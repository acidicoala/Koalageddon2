package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.*
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
fun InstallationChecklistDropdown(store: Store, openState: ChecklistOpenState, checklist: InstallationChecklist) {
    if (!openState.open.value) {
        return
    }

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
                checked = checklist.koaloaderDll,
                label = LangString("%0" to KoalaTool.Koaloader.name) { toolDll }.text
            )

            ListItem(
                checked = checklist.koaloaderConfig,
                label = LangString("%0" to KoalaTool.Koaloader.name) { toolConfig }.text
            )

            ListItem(
                checked = checklist.unlockerDll,
                label = LangString("%0" to store.unlocker.name) { toolDll }.text
            )

            ListItem(
                checked = checklist.unlockerConfig,
                label = LangString("%0" to store.unlocker.name) { toolConfig }.text
            )
        }
    }
}