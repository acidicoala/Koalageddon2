package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.core.model.ILangString
import acidicoala.koalageddon.core.model.Settings
import androidx.compose.material.SnackbarDuration
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class ShowSnackbar(override val di: DI) : DIAware {
    private val coreEventFlow: MutableSharedFlow<CoreEvent> by instance()
    private val settingsFlow: MutableStateFlow<Settings> by instance()

    suspend operator fun invoke(
        message: ILangString,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        coreEventFlow.emit(
            CoreEvent.ShowSnackbar(
                message = message.text(settingsFlow.value.strings),
                actionLabel = actionLabel,
                duration = duration,
            )
        )
    }
}