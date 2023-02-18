package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.ILangString
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun <T : ILangString> DropdownButton(
    selected: T,
    items: Array<T>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Box(modifier) {
        Button(
            onClick = { dropdownExpanded = !dropdownExpanded },
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(Modifier.width(8.dp))
            }

            Text(text = selected.text)

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = selected.text,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onSelect(item)
                        dropdownExpanded = false
                    }
                ) {
                    Text(
                        text = item.text,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}