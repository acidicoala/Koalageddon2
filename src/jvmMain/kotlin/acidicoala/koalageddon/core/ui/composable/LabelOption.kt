package acidicoala.koalageddon.core.ui.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoOption(
    label: String,
    info: String
) {
    ControlOption(label) {
        Text(
            text = info,
            modifier = Modifier.height(56.dp).wrapContentHeight(),
        )
    }
}