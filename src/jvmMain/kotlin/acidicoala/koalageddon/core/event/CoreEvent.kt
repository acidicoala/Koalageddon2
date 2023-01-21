package acidicoala.koalageddon.core.event

import androidx.compose.material.SnackbarDuration

sealed interface CoreEvent {
    class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : CoreEvent
}