package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.io.appJson
import acidicoala.koalageddon.core.model.*
import acidicoala.koalageddon.core.model.KoalaTool.Koaloader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.io.path.*

class ModifyInstallationStatus(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()
    private val downloadAndCacheKoalaTool: DownloadAndCacheKoalaTool by instance()
    private val unzipToolDll: UnzipToolDll by instance()
    private val forceCloseProcess: ForceCloseProcess by instance()
    private val isProcessRunning: IsProcessRunning by instance()

    suspend operator fun invoke(
        store: Store,
        currentStatus: InstallationStatus
    ): Flow<ILangString> = when (currentStatus) {
        is InstallationStatus.Installed -> uninstall(store)
        is InstallationStatus.NotInstalled -> install(store)
        is InstallationStatus.Updating -> channelFlow {}
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun install(store: Store) = channelFlow {
        closeIfRunning(store)

        val koaloader = Koaloader

        val unlocker = store.unlocker

        downloadAndCacheKoalaTool(koaloader).collect(::send) // Forward events upwards
        downloadAndCacheKoalaTool(unlocker).collect(::send)

        unzipToolDll(
            tool = koaloader,
            entry = "${koaloader.originalName}-${store.isa.bitness}/${koaloader.originalName}.dll",
            destination = paths.getKoaloaderDll(store),
        )

        val koaloaderConfig = Koaloader.Config(
            autoLoad = false,
            targets = listOf(store.executable),
            modules = listOf(
                Koaloader.Module(
                    path = paths.getUnlockerDll(unlocker).absolutePathString()
                )
            )
        )

        appJson.encodeToStream(koaloaderConfig, paths.getKoaloaderConfig(store).outputStream())

        unzipToolDll(
            tool = unlocker,
            entry = "${unlocker.originalName}${store.isa.bitnessSuffix}.dll",
            destination = paths.getUnlockerDll(unlocker),
        )

        unlocker.writeConfig(path = paths.getUnlockerConfig(unlocker), unlocker.defaultConfig)
    }

    private fun uninstall(store: Store) = channelFlow<ILangString> {
        closeIfRunning(store)

        paths.getKoaloaderConfig(store).deleteIfExists()

        paths.getKoaloaderDll(store).deleteIfExists()
    }

    private suspend fun closeIfRunning(store: Store) {
        paths.getStoreExecutablePath(store).let { processPath ->
            if (isProcessRunning(processPath)) {
                forceCloseProcess(processPath)
            }
        }
    }
}