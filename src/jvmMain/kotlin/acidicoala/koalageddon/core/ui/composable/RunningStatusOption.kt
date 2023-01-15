package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RunningStatusOption(
    label: String,
    running: Boolean,
) {
    val strings = LocalStrings.current

    ControlOption(label) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = null,
                tint = if (running) MaterialTheme.colors.primary else Color.Red,
                modifier = Modifier.size(8.dp)
            )

            Spacer(Modifier.width(4.dp))

            Text(
                text = if (running)
                    strings.processStatusRunning else
                    strings.processStatusNotRunning
            )
        }
    }
}