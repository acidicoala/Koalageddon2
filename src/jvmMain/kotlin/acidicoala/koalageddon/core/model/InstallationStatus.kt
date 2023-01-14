package acidicoala.koalageddon.core.model

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pending
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class InstallationStatus(
    val icon: ImageVector,
    val color: @Composable () -> Color,
    val label: ILangString
) {
    object NotInstalled : InstallationStatus(
        icon = Icons.Default.Close,
        color = { Color.Red },
        label = LangString { notInstalled }
    )

    class Installed(version: String) : InstallationStatus(
        icon = Icons.Default.Check,
        color = { MaterialTheme.colors.primary },
        label = LangString("%0" to version) { installed }
    )

    object Updating : InstallationStatus(
        icon = Icons.Default.Pending,
        color = { Color.Gray },
        label = LangString { updating }
    )

    companion object {
        fun fromBoolean(value: Boolean) = when (value) {
            false -> NotInstalled
            true -> Installed("")
        }
    }
}
