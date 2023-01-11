package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.steam.domain.use_case.ReloadSteamConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class SteamScreenModel(override val di: DI) : DIAware {
    private val scope: CoroutineScope by di.instance()
    private val reloadSteamConfig: ReloadSteamConfig by instance()

    private val mutex = Mutex()

    fun onReloadConfig() {
        scope.launch {
            mutex.withLock {
                reloadSteamConfig()
            }
        }
    }
}