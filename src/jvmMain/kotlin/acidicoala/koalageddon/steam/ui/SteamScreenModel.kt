package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.model.ILangString
import acidicoala.koalageddon.core.model.InstallationChecklist
import acidicoala.koalageddon.core.model.Store
import acidicoala.koalageddon.core.use_case.GetInstallationChecklist
import acidicoala.koalageddon.core.use_case.ModifyInstallationStatus
import acidicoala.koalageddon.steam.domain.use_case.ReloadSteamConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SteamScreenModel(
    override val di: DI,
    private val stateFlow: MutableStateFlow<State> = MutableStateFlow(State())
) : DIAware, StateFlow<SteamScreenModel.State> by stateFlow {
    data class State(
        val installProgressMessage: ILangString? = null,
        val installationChecklist: InstallationChecklist = InstallationChecklist()
    )

    private val reloadSteamConfig: ReloadSteamConfig by instance()
    private val getInstallationChecklist: GetInstallationChecklist by instance()
    private val modifyInstallationStatus: ModifyInstallationStatus by instance()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()

    fun onRefreshStatus() {
        scope.launch {
            val checklist = getInstallationChecklist(
                store = Store.Steam,
            )

            stateFlow.update {
                it.copy(installationChecklist = checklist)
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
        scope.launch {
            modifyInstallationStatus(
                store = Store.Steam,
                currentStatus = value.installationChecklist.installationStatus
            ).collect { langString ->
                stateFlow.update {
                    it.copy(installProgressMessage = langString)
                }
            }

            stateFlow.update {
                it.copy(installProgressMessage = null)
            }
        }
    }
}
