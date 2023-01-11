package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.runtime.Composable

interface ITextString {
    fun text(strings: Strings): String

    val text: String
        @Composable
        get() = text(LocalStrings.current)
}

class TextString(private val factory: (strings: Strings) -> String) : ITextString {
    override fun text(strings: Strings) = factory(strings)
}