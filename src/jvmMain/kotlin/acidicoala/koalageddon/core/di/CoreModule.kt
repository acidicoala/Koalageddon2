package acidicoala.koalageddon.core.di

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.use_case.SendIPCRequest
import acidicoala.koalageddon.core.use_case.ShowSnackbar
import kotlinx.coroutines.flow.MutableSharedFlow
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val coreModule = DI.Module(name = "Core") {
    bindSingleton { AppPaths() }
    bindSingleton { AppLogger(di) }
    bindSingleton { MutableSharedFlow<CoreEvent>() }

    // Use cases
    bindProvider { ShowSnackbar(di) }
    bindProvider { SendIPCRequest(di) }
}