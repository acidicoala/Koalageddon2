package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.*
import acidicoala.koalageddon.core.model.KoalaTool.Koaloader
import com.sun.jna.Memory
import com.sun.jna.platform.win32.Version
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import dorkbox.peParser.PE
import dorkbox.peParser.misc.MagicNumberType
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.io.FileFilter
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

class GetInstallationChecklist(override val di: DI) : DIAware {
    private val logger: AppLogger by instance()
    private val paths: AppPaths by instance()

    operator fun invoke(store: Store): InstallationChecklist {
        return try {
            InstallationChecklist(
                koaloaderDll = checkLoaderDll(store),
                koaloaderConfig = checkKoaloaderConfig(store),
                unlockerDll = checkUnlockerDll(store),
                unlockerConfig = checkUnlockerConfig(store),
            )
        } catch (e: Exception) {
            logger.error(e, "Error getting installation checklist")
            InstallationChecklist()
        }
    }

    private fun checkLoaderDll(store: Store): Boolean = store.directory.toFile()
        .listFiles(FileFilter { it.isFile && it.extension.equals("dll", ignoreCase = true) })
        ?.any { file ->
            try {
                Version.INSTANCE.run {
                    val pe = PE(file.absolutePath)

                    if (!pe.matches(store.isa)) {
                        return@any false
                    }

                    val productName = getFileInfoString(file.toPath(), "ProductName")
                        ?: return@any false

                    val isKoaloader = productName.equals("Koaloader", ignoreCase = false)

                    if (!isKoaloader) {
                        return@any false
                    }

                    return@any if (file.name.equals("version.dll", ignoreCase = true)) {
                        logger.debug("""Found Koaloader DLL at "${file.absolutePath}"""")
                        true
                    } else {
                        logger.debug(
                            """Found unexpected Koaloader DLL at "${file.absolutePath}". """ +
                                    "Please consider deleting it manually because " +
                                    "it might interfere with Koalageddon integration"
                        )
                        false
                    }
                }
            } catch (e: Exception) {
                logger.error(e, "Error checking file ${file.absolutePath}")
                false
            }
        } ?: false

    /**
     * @return Path to an unlocker DLL
     */
    private fun checkKoaloaderConfig(store: Store): Boolean {
        val configFile = paths.getKoaloaderConfig(store)

        if (!configFile.exists()) {
            logger.debug("Koaloader config for $store not found in $configFile")
            return false
        }

        logger.debug("""Found Koaloader config at "$configFile"""")

        val config = try {
            Koaloader.parseConfig(configFile)
        } catch (e: Exception) {
            logger.error(e, "Koaloader config parsing error")
            return false
        }

        if (!config.enabled) {
            logger.warn("Koaloader config should not be disabled")
            return false
        }

        if (config.autoLoad) {
            logger.warn("Koaloader config should have autoload disabled")
            return false
        }

        if (config.targets.size != 1 || !config.targets.first().equals(store.executable, ignoreCase = true)) {
            logger.warn("Koaloader config target is misconfigured")
            return false
        }

        if (config.modules.none { Path(it.path) == paths.getUnlockerDll(store.unlocker) }) {
            logger.warn("Koaloader config module is misconfigured")
            return false
        }

        return true
    }

    /**
     * @return Unlocker version encoded in DLL
     */
    private fun checkUnlockerDll(store: Store): String? {
        val unlockerPath = paths.getUnlockerDll(store.unlocker)

        if (!unlockerPath.exists()) {
            logger.debug("""Unlocker DLL not found at "$unlockerPath"""")
            return null
        }

        logger.debug("""Unlocker DLL found at "$unlockerPath"""")

        val pe = PE(unlockerPath.toString())
        if (!pe.matches(store.isa)) {
            logger.debug("Found Unlocker DLL with incompatible architecture")
            return null
        }

        return getFileInfoString(unlockerPath, "ProductVersion")
    }

    private fun checkUnlockerConfig(store: Store): Boolean {
        val configPath = paths.getUnlockerConfig(store.unlocker)

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

    private fun getFileInfoString(path: Path, key: String): String? {
        Version.INSTANCE.apply {
            val absolutePath = path.absolutePathString()

            when (val bufferSize = GetFileVersionInfoSize(absolutePath, null)) {
                0 -> {
                    logger.trace("GetFileVersionInfoSize returned 0 for $path")
                    return null
                }
                else -> {
                    val buffer = Memory(bufferSize.toLong())

                    if (!GetFileVersionInfo(absolutePath, 0, bufferSize, buffer)) {
                        logger.trace("GetFileVersionInfo returned false for $path")
                        return null
                    }

                    val stringPointer = PointerByReference()
                    val stringSize = IntByReference()
                    val subBlock = """\StringFileInfo\040904E4\$key"""

                    if (!VerQueryValue(buffer, subBlock, stringPointer, stringSize)) {
                        logger.trace("VerQueryValue returned false for $path")
                        return null
                    }

                    return try {
                        stringPointer.value.getWideString(0)
                    } catch (e: Exception) {
                        logger.error(e, "Failed to get key $key from string pointer")
                        null
                    }
                }
            }
        }
    }
}