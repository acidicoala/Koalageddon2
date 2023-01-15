package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.ILangString
import androidx.compose.runtime.Composable

@Composable
fun <T : ILangString> DropdownOption(label: String, items: Array<T>, selected: T, onSelect: (T) -> Unit) {
    ControlOption(label) {
        DropdownButton(
            selected = selected,
            items = items,
            onSelect = onSelect,
        )
    }
}
