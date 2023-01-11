package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.BuildConfig
import net.harawata.appdirs.AppDirsFactory
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.div

class AppPaths {
    private val dataPath = Path(
        AppDirsFactory.getInstance().getUserDataDir(BuildConfig.APP_NAME, "", BuildConfig.APP_AUTHOR)
    ).apply {
        createDirectories()
    }

    val settings = dataPath / "Koalageddon.settings.json"
    val log = dataPath / "Koalageddon.log.log"
}