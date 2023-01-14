package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.io.appJson
import acidicoala.koalageddon.core.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.encodeToStream
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import kotlin.io.path.*

class ModifyInstallationStatus(override val di: DI) : DIAware {
    private val downloadAndCacheKoalaTool: DownloadAndCacheKoalaTool by instance()
    private val unzipToolDll: UnzipToolDll by instance()
    private val paths: AppPaths by instance()

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
        val koaloader = KoalaTool.Koaloader

        val unlocker = store.unlocker
        val unlockerFileName = "${unlocker.name}${store.isa.bitness}.dll"
        val unlockerDirectory = (paths.unlockers / unlocker.name).createDirectories()
        val unlockerDestination = unlockerDirectory / unlockerFileName

        downloadAndCacheKoalaTool(koaloader).collect(::send) // Forward events upwards
        downloadAndCacheKoalaTool(unlocker).collect(::send)

        unzipToolDll(
            tool = koaloader,
            entry = "${koaloader.originalName}-${store.isa.bitness}/${koaloader.originalName}.dll",
            destination = store.directory / "${koaloader.originalName}.dll",
        )

        val koaloaderConfig = KoalaTool.Koaloader.Config(
            autoLoad = false,
            targets = listOf(store.executable),
            modules = listOf(
                KoalaTool.Koaloader.Module(
                    path = unlockerDestination.absolutePathString()
                )
            )
        )

        val koaloaderConfigPath = store.directory / koaloader.configName
        appJson.encodeToStream(koaloaderConfig, koaloaderConfigPath.outputStream())

        unzipToolDll(
            tool = unlocker,
            entry = "${unlocker.originalName}${store.isa.bitnessSuffix}.dll",
            destination = unlockerDestination,
        )

        unlocker.writeDefaultConfig(path = unlockerDirectory / unlocker.configName)
    }

    private suspend fun uninstall(store: Store) = channelFlow<ILangString> {

    }
}