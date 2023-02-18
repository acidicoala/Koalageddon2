package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.ILangString
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun <T : ILangString> DropdownOption(
    label: String,
    items: Array<T>,
    selected: T,
    onSelect: (T) -> Unit,
    icon: ImageVector? = null,
) {
    ControlOption(label) {
        DropdownButton(
            selected = selected,
            items = items,
            onSelect = onSelect,
            icon = icon,
        )
    }
}
