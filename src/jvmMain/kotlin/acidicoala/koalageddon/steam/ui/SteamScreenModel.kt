package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.InstallationChecklist
import acidicoala.koalageddon.core.model.Store
import acidicoala.koalageddon.steam.domain.use_case.GetInstallationChecklist
import acidicoala.koalageddon.steam.domain.use_case.ReloadSteamConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.time.Duration.Companion.seconds

class SteamScreenModel(
    override val di: DI,
    private val stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
) : DIAware, StateFlow<SteamScreenModel.State> by stateFlow {
    data class State(
        val installationChecklist: InstallationChecklist = InstallationChecklist()
    )

    private val reloadSteamConfig: ReloadSteamConfig by instance()
    private val getInstallationChecklist: GetInstallationChecklist by instance()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()

    init {
        // Begin polling the installation status every second
        scope.launch {
            while (true) {
                val checklist = getInstallationChecklist(
                    store = Store.Steam,
                )

                stateFlow.update {
                    it.copy(installationChecklist = checklist)
                }

                delay(1.seconds)
            }
        }
    }

    fun onReloadConfig() {
        scope.launch {
            mutex.withLock {
                reloadSteamConfig()
            }
        }
    }

    fun onModifyInstallation() {

    }

}