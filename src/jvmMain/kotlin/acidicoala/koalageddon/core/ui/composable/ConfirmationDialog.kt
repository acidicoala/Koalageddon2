package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.OpenState
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmationDialog(
    openState: OpenState,
    title: String,
    message: String,
    onOk: () -> Unit,
    onCancel: () -> Unit = {}
) {
    if (!openState.open.value) {
        return
    }

    val strings = LocalStrings.current

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = {
                    openState.close()
                    onOk()
                }
            ) {
                Text(text = strings.ok)
            }
        },
        modifier = Modifier.width(DefaultMaxWidth),
        dismissButton = {
            Button(
                onClick = {
                    openState.close()
                    onCancel()
                }
            ) {
                Text(text = strings.cancel)
            }
        }
    )
}