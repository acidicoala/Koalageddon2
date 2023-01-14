package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.io.path.createDirectories
import kotlin.io.path.div

class ModifyInstallationStatus(override val di: DI) : DIAware {
    private val downloadAndCacheKoalaTool: DownloadAndCacheKoalaTool by instance()
    private val unzipToolDll: UnzipToolDll by instance()
    private val paths: AppPaths by instance()

    suspend operator fun invoke(
        store: Store,
        currentStatus: InstallationStatus
    ): Flow<ILangString> = when (currentStatus) {
        InstallationStatus.Installed -> uninstall(store)
        InstallationStatus.NotInstalled -> install(store)
    }

    private suspend fun install(store: Store) = channelFlow {
        downloadAndCacheKoalaTool(KoalaTool.Koaloader).collect(::send) // Forward events upwards
        downloadAndCacheKoalaTool(store.unlocker).collect(::send)

        val koaloader = KoalaTool.Koaloader
        unzipToolDll(
            tool = koaloader,
            entry = "${koaloader.originalName}-${store.isa.bitness}/${koaloader.originalName}.dll",
            destination = store.directory / "${koaloader.originalName}.dll",
        )

        val unlockerFileName = "${store.unlocker.name}${store.isa.bitness}.dll"
        val unlockerDirectory = (paths.unlockers / store.unlocker.name).createDirectories()
        unzipToolDll(
            tool = store.unlocker,
            entry = "${store.unlocker.originalName}${store.isa.bitnessSuffix}.dll",
            destination = unlockerDirectory / unlockerFileName,
        )
    }

    private suspend fun uninstall(store: Store) = channelFlow<ILangString> {

    }
}