package acidicoala.koalageddon.core.ui.composable

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ButtonOption(
    label: String,
    buttonLabel: String,
    buttonIcon: ImageVector? = null,
    enabled: Boolean = true,
    outlined: Boolean = false,
    onClick: () -> Unit
) {
    ControlOption(label) {
        val buttonContent: @Composable () -> Unit = {
            buttonIcon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = label
                )

                DefaultSpacer()
            }

            Text(text = buttonLabel)
        }

        if (outlined) {
            OutlinedButton(onClick = onClick, enabled = enabled) {
                buttonContent()
            }
        } else {
            Button(onClick = onClick, enabled = enabled) {
                buttonContent()
            }
        }
    }
}