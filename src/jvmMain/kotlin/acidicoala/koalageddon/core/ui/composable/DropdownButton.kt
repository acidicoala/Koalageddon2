package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.ILangString
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T : ILangString> DropdownButton(
    selected: T,
    items: Array<T>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Box(modifier) {
        Button(
            onClick = { dropdownExpanded = !dropdownExpanded },
        ) {

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