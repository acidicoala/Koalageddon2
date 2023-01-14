package acidicoala.koalageddon.core.di

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.use_case.*
import acidicoala.koalageddon.settings.domain.use_case.ReadSettings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.bindEagerSingleton
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val coreModule = DI.Module(name = "Core") {
    bindSingleton { AppPaths() }
    bindEagerSingleton { AppLogger(di) }
    bindSingleton { MutableSharedFlow<CoreEvent>() }
    bindSingleton { MutableStateFlow(ReadSettings(di)()) }

    // Use cases
    bindProvider { ShowSnackbar(di) }
    bindProvider { SendPipeRequest(di) }
    bindProvider { OpenDirectoryInExplorer(di) }
    bindProvider { GetInstallationChecklist(di) }
    bindProvider { ModifyInstallationStatus(di) }
    bindProvider { DownloadAndCacheKoalaTool(di) }
    bindProvider { UnzipToolDll(di) }
}