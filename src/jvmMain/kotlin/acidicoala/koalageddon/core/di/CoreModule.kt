package acidicoala.koalageddon.core.di

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.use_case.*
import kotlinx.coroutines.flow.MutableSharedFlow
import org.kodein.di.DI
import org.kodein.di.bindEagerSingleton
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val coreModule = DI.Module(name = "Core") {
    bindSingleton { AppPaths() }
    bindEagerSingleton { AppLogger(di) }
    bindSingleton { MutableSharedFlow<CoreEvent>() }

    // Use cases
    bindProvider { ShowSnackbar(di) }
    bindProvider { SendPipeRequest(di) }
    bindProvider { OpenDirectoryInExplorer(di) }
    bindProvider { GetInstallationChecklist(di) }
    bindProvider { ModifyInstallationStatus(di) }
}