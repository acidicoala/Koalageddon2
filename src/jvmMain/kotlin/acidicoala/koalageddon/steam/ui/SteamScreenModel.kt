package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.*
import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI
import acidicoala.koalageddon.core.model.KoalaTool.SmokeAPI.Config
import acidicoala.koalageddon.core.use_case.GetInstallationChecklist
import acidicoala.koalageddon.core.use_case.ModifyInstallationStatus
import acidicoala.koalageddon.core.use_case.ShowSnackbar
import acidicoala.koalageddon.core.use_case.UpdateUnlockerConfig
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
        val installationChecklist: InstallationChecklist? = null,
        val config: Config? = null
    )

    private val paths: AppPaths by instance()
    private val logger: AppLogger by instance()
    private val reloadSteamConfig: ReloadSteamConfig by instance()
    private val getInstallationChecklist: GetInstallationChecklist by instance()
    private val showSnackbar: ShowSnackbar by instance()
    private val updateUnlockerConfig: UpdateUnlockerConfig by instance()
    private val modifyInstallationStatus: ModifyInstallationStatus by instance()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()

    fun onRefreshState() {
        scope.launch {
            stateFlow.update {
                it.copy(
                    installationChecklist = null,
                    config = null
                )
            }

            stateFlow.update {
                it.copy(
                    installationChecklist = getInstallationChecklist(store = Store.Steam),
                    config = SmokeAPI.parseConfig(paths.getUnlockerConfig(SmokeAPI))
                )
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
            value.installationChecklist?.installationStatus?.let { currentStatus ->
                if (currentStatus is InstallationStatus.Updating) {
                    return@let
                }

                try {
                    modifyInstallationStatus(
                        store = Store.Steam, currentStatus = currentStatus
                    ).collect { langString ->
                        stateFlow.update {
                            it.copy(installProgressMessage = langString)
                        }
                    }

                    showSnackbar(message = LangString {
                        when (currentStatus) {
                            is InstallationStatus.Installed -> removalSuccess
                            is InstallationStatus.NotInstalled -> installationSuccess
                            is InstallationStatus.Updating -> throw IllegalStateException()
                        }
                    })
                } catch (e: Exception) {
                    logger.error(e, "Error modifying installation status")
                    showSnackbar(message = LangString {
                        when (currentStatus) {
                            is InstallationStatus.Installed -> removalError
                            is InstallationStatus.NotInstalled -> installationError
                            is InstallationStatus.Updating -> throw IllegalStateException()
                        }
                    })
                } finally {
                    stateFlow.update {
                        it.copy(installProgressMessage = null)
                    }

                    onRefreshState()
                }
            }
        }
    }

    fun onConfigChange(config: Config) {
        stateFlow.update {
            it.copy(config = config)
        }

        updateUnlockerConfig(config, SmokeAPI)
    }
}
