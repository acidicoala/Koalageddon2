package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.event.CoreEvent
import androidx.compose.material.SnackbarDuration
import kotlinx.coroutines.flow.MutableSharedFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class ShowSnackbar(override val di: DI) : DIAware {
    private val coreEventFlow: MutableSharedFlow<CoreEvent> by instance()

    suspend operator fun invoke(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        coreEventFlow.emit(
            CoreEvent.ShowSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
            )
        )
    }
}