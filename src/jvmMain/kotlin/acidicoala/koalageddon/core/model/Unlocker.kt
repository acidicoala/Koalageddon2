package acidicoala.koalageddon.core.model

import acidicoala.koalageddon.core.serialization.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonObject
import java.nio.file.Path

sealed class Unlocker(
    val dllName: String,
    val configName: String
) {
    interface IConfig

    abstract fun parseConfig(path: Path): IConfig

    object SmokeAPI : Unlocker(
        dllName = "SmokeAPI.dll",
        configName = "SmokeAPI.config.json"
    ) {
        @Serializable
        enum class AppStatus {
            @SerialName("original")
            Original,

            @SerialName("unlocked")
            Unlocked,

            @SerialName("locked")
            Locked,
        }

        @Serializable
        data class App(
            val dlcs: Map<String, String> = mapOf()
        )

        @Serializable
        data class Config(
            @SerialName("\$version")
            val version: Int = 2,
            val logging: Boolean = false,
            @SerialName("unlock_family_sharing")
            val unlockFamilySharing: Boolean = true,
            @SerialName("default_app_status")
            val defaultAppStatus: AppStatus = AppStatus.Unlocked,
            @SerialName("override_app_status")
            val overrideAppStatus: Map<String, AppStatus> = mapOf(),
            @SerialName("override_dlc_status")
            val overrideDlcStatus: Map<String, AppStatus> = mapOf(),
            @SerialName("extra_dlcs")
            val extraDlcs: Map<String, App> = mapOf(),
            @SerialName("auto_inject_inventory")
            val autoInjectInventory: Boolean = true,
            @SerialName("extra_inventory_items")
            val extraInventoryItems: List<Int> = listOf(),
            @SerialName("store_config")
            val storeConfig: JsonObject? = null,
        ) : IConfig

        override fun parseConfig(path: Path) = json.decodeFromString<Config>(path.toFile().readText())
    }
}