package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.runtime.Composable

interface TextString {
    fun factory(strings: Strings): String

    val text: String
        @Composable
        get() = factory(LocalStrings.current)
}