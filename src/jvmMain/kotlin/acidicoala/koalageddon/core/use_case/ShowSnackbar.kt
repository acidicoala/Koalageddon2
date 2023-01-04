package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.event.CoreEvent
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import kotlinx.coroutines.flow.MutableSharedFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class ShowSnackbar(override val di: DI) : DIAware {
    private val coreEventFlow: MutableSharedFlow<CoreEvent> by instance()

    suspend operator fun invoke(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        coreEventFlow.emit(
            CoreEvent.ShowSnackbar(object : SnackbarVisuals {
                override val actionLabel = actionLabel
                override val duration = duration
                override val message = message
                override val withDismissAction = withDismissAction
            })
        )
    }
}