package acidicoala.koalageddon.core.event

import acidicoala.koalageddon.core.model.ILangString
import androidx.compose.material.SnackbarDuration

sealed interface CoreEvent {
    class ShowSnackbar(
        val message: ILangString,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : CoreEvent
}