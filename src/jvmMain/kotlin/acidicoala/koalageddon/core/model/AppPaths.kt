package acidicoala.koalageddon.core.model

import net.harawata.appdirs.AppDirsFactory
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.div

class AppPaths {
    private val appName = "koalageddon"
    private val appVersion = ""
    private val appAuthor = "acidicoala"

    private val appDirs = AppDirsFactory.getInstance();

    private val userConfigPath = Path(appDirs.getUserConfigDir(appName, appVersion, appAuthor))

    val settings = userConfigPath / "settings.json"

    init {
        userConfigPath.createDirectories()
    }
}