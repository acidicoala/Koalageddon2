package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.values.Strings
import androidx.compose.runtime.Composable

/**
 * Encapsulates a string reference in a language-agnostic manner.
 * Use [LangString] to instantiate a new instance of [ILangString].
 */
interface ILangString {
    fun Strings.text(): String

    val text: String
        @Composable
        get() = LocalStrings.current.text()
}

class LangString(
    private vararg val placeholders: Pair<String, String>,
    private val factory: Strings.() -> String
) : ILangString {
    override fun Strings.text() = placeholders.fold(initial = factory(this)) { acc, (key, value) ->
        acc.replace(key, value)
    }
}