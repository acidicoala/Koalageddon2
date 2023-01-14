package acidicoala.koalageddon.core.ui.composable

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ButtonOption(
    label: String,
    buttonLabel: String,
    buttonIcon: ImageVector? = null,
    onClick: () -> Unit
) {
    ControlOption(label) {
        Button(onClick = onClick) {
            buttonIcon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = label
                )

                DefaultSpacer()
            }

            Text(text = buttonLabel)
        }
    }
}