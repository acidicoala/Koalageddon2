package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.TextString
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun <T : TextString> DropdownOption(label: String, items: Array<T>, selected: T, onSelect: (T) -> Unit) {
    val strings = LocalStrings.current

    ControlOption(label) {
        var dropdownExpanded by remember { mutableStateOf(false) }

        Box {
            Button(
                onClick = { dropdownExpanded = !dropdownExpanded },
            ) {

                Text(text = selected.factory(strings))

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = label,
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
}