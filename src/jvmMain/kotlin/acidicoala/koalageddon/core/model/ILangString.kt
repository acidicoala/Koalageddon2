package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.runtime.Composable

/**
 * Encapsulates a string reference in a language-agnostic manner.
 * Use [LangString] to instantiate a new instance of [ILangString].
 */
interface ILangString {
    fun text(strings: Strings): String

    val text: String
        @Composable
        get() = text(LocalStrings.current)
}

class LangString(
    private vararg val placeholders: Pair<String, String>,
    private val factory: Strings.() -> String
) : ILangString {
    override fun text(strings: Strings) = placeholders.fold(initial = factory(strings)) { acc, (key, value) ->
        acc.replace(key, value)
    }
}