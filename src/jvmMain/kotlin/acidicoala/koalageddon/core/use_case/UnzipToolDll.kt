package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.model.KoalaTool
import acidicoala.koalageddon.core.model.SemanticVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.io.FileFilter
import java.nio.file.Path
import java.util.zip.ZipFile
import kotlin.io.path.outputStream

class UnzipToolDll(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()
    private val logger: AppLogger by instance()

    /**
     * Unzips the given zip entry from a cache file determined by [KoalaTool] into the given directory
     */
    suspend operator fun invoke(tool: KoalaTool, entry: String, destination: Path) {
        withContext(Dispatchers.IO) {
            val zipFile = paths.cacheDir.toFile()
                .listFiles(FileFilter { it.extension.equals("zip", ignoreCase = true) })
                ?.filter { it.name.startsWith(tool.name, ignoreCase = true) }
                ?.mapNotNull { file -> SemanticVersion.fromVersion(file.name)?.let { version -> file to version } }
                ?.filter { (_, zipVersion) -> tool.majorVersion == zipVersion.major }
                ?.maxByOrNull { (_, version) -> version }
                ?.first
                ?: throw IllegalStateException("${tool.name} release zip not found in cache")

            logger.debug("Unzipping $entry from $zipFile into $destination")

            ZipFile(zipFile).use { zip ->
                val zipEntry = zip.getEntry(entry)
                    ?: throw IllegalStateException("${zipFile.absolutePath} does not contain entry: $entry")

                zip.getInputStream(zipEntry).use { input ->
                    destination.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
}