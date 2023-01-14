package acidicoala.koalageddon.core.model

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class InstallationStatus(
    val icon: ImageVector,
    val color: @Composable () -> Color
) {
    object NotInstalled : InstallationStatus(icon = Icons.Default.Close, color = { Color.Red })
    object Installed : InstallationStatus(icon = Icons.Default.Check, color = { MaterialTheme.colors.primary })

    companion object {
        fun fromBoolean(value: Boolean) = when (value) {
            false -> NotInstalled
            true -> Installed
        }
    }
}
