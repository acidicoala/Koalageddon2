package acidicoala.koalageddon.core.ui.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SectionLabel(icon: ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colors.onSurface,
        )

        DefaultSpacer()

        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}