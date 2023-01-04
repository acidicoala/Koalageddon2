package acidicoala.koalageddon.core.ui.composable

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ButtonOption(
    label: String,
    buttonLabel: String,
    onClick: () -> Unit
) {
    ControlOption(label) {
        Button(onClick = onClick) {
            Text(text = buttonLabel)
        }
    }
}