package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.BuildConfig
import net.harawata.appdirs.AppDirsFactory
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.div

class AppPaths {
    val data = Path(
        AppDirsFactory.getInstance().getUserDataDir(BuildConfig.APP_NAME, "", BuildConfig.APP_AUTHOR)
    ).createDirectories()

    val settings = data / "Koalageddon.settings.json"

    val log = data / "Koalageddon.log.log"

    val cache = (data / "cache").createDirectories()

    val unlockers = (data / "unlockers").createDirectories()
}