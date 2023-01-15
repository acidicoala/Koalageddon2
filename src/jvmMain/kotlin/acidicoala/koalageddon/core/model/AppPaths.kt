package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.BuildConfig
import acidicoala.koalageddon.core.model.KoalaTool.Koaloader
import net.harawata.appdirs.AppDirsFactory
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.div

class AppPaths {
    val dataDir = Path(
        AppDirsFactory.getInstance().getUserDataDir(BuildConfig.APP_NAME, "", BuildConfig.APP_AUTHOR)
    ).createDirectories()

    val settings = dataDir / "Koalageddon.settings.json"

    val log = dataDir / "Koalageddon.log.log"

    val cacheDir = (dataDir / "cache").createDirectories()

    private val unlockersDir = (dataDir / "unlockers").createDirectories()

    private fun getUnlockerDir(unlocker: KoalaTool) = (unlockersDir / unlocker.name).createDirectories()

    fun getKoaloaderConfig(store: Store) = store.directory / Koaloader.configName

    fun getKoaloaderDll(store: Store) = store.directory / "${Koaloader.originalName}.dll"

    fun getUnlockerConfig(unlocker: KoalaTool) = getUnlockerDir(unlocker) / unlocker.configName

    fun getUnlockerDll(unlocker: KoalaTool) = getUnlockerDir(unlocker) / "${unlocker.name}.dll"

    fun getCachePath(filename: String) = cacheDir / filename
}