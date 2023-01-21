package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.io.appJson
import acidicoala.koalageddon.core.values.Strings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Path
import java.util.*
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

@OptIn(ExperimentalSerializationApi::class)
sealed class KoalaTool(
    val name: String,
    val originalName: String,
    val majorVersion: Int
) {
    val configName = "$name.config.json"
    val gitHubReleaseUrl = "https://api.github.com/repos/acidicoala/$name/releases"

    interface IConfig

    abstract val defaultConfig: IConfig

    abstract fun parseConfig(path: Path): IConfig

    abstract fun writeConfig(path: Path, config: IConfig)

    abstract fun writeDefaultConfig(path: Path)

    object Koaloader : KoalaTool(name = "Koaloader", originalName = "version", majorVersion = 3) {
        @Serializable
        data class Module(
            val path: String = "",
            val required: Boolean = true,
        )

        @Serializable
        data class Config(
            val logging: Boolean = false,
            val enabled: Boolean = true,
            @SerialName("auto_load") val autoLoad: Boolean = true,
            val targets: List<String> = listOf(),
            val modules: List<Module> = listOf(),
        ) : IConfig

        override val defaultConfig = Config()

        override fun parseConfig(path: Path) = appJson.decodeFromStream<Config>(path.inputStream())

        override fun writeConfig(path: Path, config: IConfig) =
            appJson.encodeToStream(config as Config, path.outputStream())

        override fun writeDefaultConfig(path: Path) = appJson.encodeToStream(Config(), path.outputStream())
    }

    object SmokeAPI : KoalaTool(name = "SmokeAPI", originalName = "steam_api", majorVersion = 2) {
        @Serializable
        enum class AppStatus : ILangString {
            @SerialName("original")
            Original {
                override fun text(strings: Strings) = strings.appStatusOriginal
            },

            @SerialName("unlocked")
            Unlocked {
                override fun text(strings: Strings) = strings.appStatusUnlocked
            },

            @SerialName("locked")
            Locked {
                override fun text(strings: Strings) = strings.appStatusLocked
            };

            companion object {
                val validAppStatuses = arrayOf(Original, Unlocked)
                val validDlcStatuses = arrayOf(Original, Unlocked, Locked)
            }
        }

        @Serializable
        data class App(
            val dlcs: Map<String, String> = mapOf()
        )

        // TODO: Version validation
        @Serializable
        data class Config(
            val logging: Boolean = false,
            @SerialName("unlock_family_sharing") val unlockFamilySharing: Boolean = true,
            @SerialName("default_app_status") val defaultAppStatus: AppStatus = AppStatus.Unlocked,
            @SerialName("override_app_status") val overrideAppStatus: Map<String, AppStatus> = mapOf(),
            @SerialName("override_dlc_status") val overrideDlcStatus: Map<String, AppStatus> = mapOf(),
            @SerialName("auto_inject_inventory") val autoInjectInventory: Boolean = true,
            @SerialName("extra_inventory_items") val extraInventoryItems: List<Int> = listOf(),
            @SerialName("\$version") val version: Int = 2,
            @SerialName("store_config") val storeConfig: JsonObject? = null,
            @SerialName("extra_dlcs") val extraDlcs: Map<String, App> = mapOf(),
        ) : IConfig

        override val defaultConfig = Config()

        override fun parseConfig(path: Path) = appJson.decodeFromStream<Config>(path.inputStream())

        override fun writeConfig(path: Path, config: IConfig) =
            appJson.encodeToStream(config as Config, path.outputStream())

        override fun writeDefaultConfig(path: Path) = appJson.encodeToStream(Config(), path.outputStream())
    }
}