package acidicoala.koalageddon.core.ui.composable

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable

@Composable
fun SwitchOption(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ControlOption(label) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}