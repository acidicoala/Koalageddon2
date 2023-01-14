package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.ISA
import acidicoala.koalageddon.core.model.InstallationChecklist
import acidicoala.koalageddon.core.model.Store
import acidicoala.koalageddon.core.serialization.json
import com.sun.jna.Memory
import com.sun.jna.platform.win32.Version
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import dorkbox.peParser.PE
import dorkbox.peParser.misc.MagicNumberType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.exists

class GetInstallationChecklist(override val di: DI) : DIAware {

    @Serializable
    data class Module(
        val path: String = "",
        val required: Boolean = true,
    )

    @Serializable
    data class KoaloaderConfig(
        val logging: Boolean = false,
        val enabled: Boolean = true,
        @SerialName("auto_load")
        val autoLoad: Boolean = true,
        val targets: List<String> = listOf(),
        val modules: List<Module> = listOf(),
    )

    private val logger: AppLogger by instance()

    operator fun invoke(store: Store): InstallationChecklist {
        var checklist = InstallationChecklist()

        try {
            checkLoaderDll(store).let { loaderDll ->
                checklist = checklist.copy(loaderDll = loaderDll)

                if (!loaderDll) {
                    return checklist
                }
            }

            val unlockerPath = checkLoaderConfig(store).let { result ->
                when {
                    result.isSuccess -> {
                        checklist = checklist.copy(loaderConfig = true)
                        result.getOrThrow()
                    }
                    else -> {
                        logger.debug(result.exceptionOrNull()?.message ?: "Unknown loader config error")
                        return checklist.copy(loaderConfig = false)
                    }
                }
            }

            checkUnlockerDll(store, unlockerPath).let { result ->
                when {
                    result.isSuccess -> {
                        checklist = checklist.copy(
                            unlockerDll = true,
                            unlockerVersion = result.getOrThrow()
                        )
                    }
                    else -> {
                        logger.debug(result.exceptionOrNull()?.message ?: "Unknown unlocker dll error")
                        return checklist.copy(unlockerDll = false)
                    }
                }
            }

            checkUnlockerConfig(store, unlockerPath).let { unlockerConfig ->
                checklist = checklist.copy(unlockerConfig = unlockerConfig)

                if (!unlockerConfig) {
                    return checklist
                }
            }

            return checklist
        } catch (e: Exception) {
            logger.error(e, "Error getting installation checklist")
            return checklist
        }
    }

    private fun checkLoaderDll(store: Store): Boolean = store.path.toFile().listFiles()
        ?.filter { it.extension.equals("dll", ignoreCase = true) }
        ?.any { file ->
            Version.INSTANCE.run {
                val pe = PE(file.absolutePath)

                if (!pe.matches(store.isa)) {
                    return@any false
                }

                val productNameResult = getFileInfoString(file.absolutePath, "ProductName")
                val productName = when {
                    productNameResult.isSuccess -> productNameResult.getOrThrow()
                    else -> {
                        val message = productNameResult.exceptionOrNull()?.message
                        logger.debug("Error getting product name from loader dll: $message")
                        return@any false
                    }
                }

                val isKoaloader = productName.equals("Koaloader", ignoreCase = false)

                if (!isKoaloader) {
                    return@any false
                }

                if (file.name.equals("version.dll", ignoreCase = true)) {
                    logger.debug("""Found Koaloader DLL at "${file.absolutePath}"""")
                    return@any true
                } else {
                    logger.debug(
                        """Found unexpected Koaloader DLL at "${file.absolutePath}". """ +
                                "Please consider deleting it manually because " +
                                "it might interfere with Koalageddon integration"
                    )
                    return@any false
                }
            }
        } ?: false

    /**
     * @return Path to an unlocker DLL
     */
    private fun checkLoaderConfig(store: Store): Result<Path> {
        val configFile = store.path.toFile().listFiles()
            ?.find { it.name.equals("Koaloader.config.json", ignoreCase = true) }
            ?: return Result.failure(Exception("Koalageddon config not found"))

        logger.debug("""Found Koalageddon config at "${configFile.absolutePath}"""")

        val config = try {
            json.decodeFromString<KoaloaderConfig>(configFile.readText())
        } catch (e: Exception) {
            return Result.failure(Exception("Failed to parse Koaloader config", e))
        }

        if (!config.enabled) {
            return Result.failure(Exception("Koaloader config is disabled"))
        }

        if (config.autoLoad) {
            return Result.failure(Exception("Koaloader config is should not be set to autoload"))
        }

        if (config.targets.size != 1 || !config.targets.first().equals(store.executable, ignoreCase = true)) {
            return Result.failure(Exception("Koaloader config target is misconfigured"))
        }

        val unlockerPath = config.modules
            .map { Path(it.path).let { path -> if (path.isAbsolute) path else store.path / path } }
            .find { path -> path.fileName.toString().equals(store.unlocker.dllName, ignoreCase = true) }
            ?: return Result.failure(Exception("Koaloader config module is misconfigured"))

        return Result.success(unlockerPath)
    }

    /**
     * @return Unlocker version encoded in DLL
     */
    private fun checkUnlockerDll(store: Store, unlockerPath: Path): Result<String> {
        if (!unlockerPath.exists()) {
            return Result.failure(Exception("""Unlocker DLL not found at "$unlockerPath""""))
        }

        logger.debug("""Unlocker DLL found at "$unlockerPath"""")

        val pe = PE(unlockerPath.toString())
        if (!pe.matches(store.isa)) {
            return Result.failure(Exception("Found Unlocker DLL with incompatible architecture"))
        }

        return getFileInfoString(unlockerPath.toString(), "ProductVersion")
    }

    private fun checkUnlockerConfig(store: Store, unlockerPath: Path): Boolean {
        val configPath = unlockerPath.parent / store.unlocker.configName

        if (!configPath.exists()) {
            logger.debug("""Unlocker config not found at "$configPath"""")
            return false
        }

        logger.debug("""Unlocker config found at "$configPath"""")

        try {
            store.unlocker.parseConfig(configPath)
        } catch (e: Exception) {
            logger.error(e, "Error parsing the unlocker config")
            return false
        }

        return true
    }

    private fun PE.matches(isa: ISA): Boolean {
        if (!isPE) {
            return false
        }

        val magicNumber = optionalHeader.MAGIC_NUMBER.get()

        return when (isa) {
            ISA.X86 -> magicNumber == MagicNumberType.PE32
            ISA.X86_64 -> magicNumber != MagicNumberType.PE32
        }
    }

    private fun getFileInfoString(path: String, key: String): Result<String> {
        Version.INSTANCE.apply {
            when (val bufferSize = GetFileVersionInfoSize(path, null)) {
                0 -> {
                    return Result.failure(Exception("GetFileVersionInfoSize returned 0"))
                }
                else -> {
                    val buffer = Memory(bufferSize.toLong())

                    if (!GetFileVersionInfo(path, 0, bufferSize, buffer)) {
                        return Result.failure(Exception("GetFileVersionInfo returned false"))
                    }

                    val stringPointer = PointerByReference()
                    val stringSize = IntByReference()

                    if (
                        !VerQueryValue(
                            buffer,
                            """\StringFileInfo\040904E4\$key""",
                            stringPointer,
                            stringSize
                        )
                    ) {
                        return Result.failure(Exception("VerQueryValue returned false"))
                    }

                    return try {
                        Result.success(stringPointer.value.getWideString(0))
                    } catch (e: Exception) {
                        Result.failure(Exception("Failed to get key $key from string pointer", e))
                    }
                }
            }
        }
    }

}