package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.model.InstallationStatus
import acidicoala.koalageddon.core.model.Store
import org.kodein.di.DI
import org.kodein.di.DIAware

class ModifyInstallationStatus(override val di: DI) : DIAware {

    operator fun invoke(store: Store, currentStatus: InstallationStatus) {
        when (currentStatus) {
            InstallationStatus.Installed -> uninstall(store)
            InstallationStatus.NotInstalled -> install(store)
        }
    }

    private fun install(store: Store) {
        
    }

    private fun uninstall(store: Store) {

    }

}