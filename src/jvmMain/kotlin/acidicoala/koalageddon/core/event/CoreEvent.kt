package acidicoala.koalageddon.core.event

import androidx.compose.material3.SnackbarVisuals

sealed interface CoreEvent {
    class ShowSnackbar(val snackbarVisuals: SnackbarVisuals) : CoreEvent
}