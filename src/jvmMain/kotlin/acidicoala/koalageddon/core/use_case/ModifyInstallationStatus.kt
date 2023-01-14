package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.io.httpClient
import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.nio.file.Files
import kotlin.io.path.div

class ModifyInstallationStatus(override val di: DI) : DIAware {

    @Serializable
    data class GitHubAsset(
        val name: String,
        val size: Long,
        @SerialName("browser_download_url")
        val browserDownloadUrl: String
    )

    @Serializable
    data class GitHubRelease(
        @SerialName("tag_name")
        val tagName: String,
        val assets: List<GitHubAsset>,
    ) {
        @Transient
        val version = SemanticVersion.fromGitTag(tagName)
    }

    private val paths: AppPaths by instance()
    private val logger: AppLogger by instance()
    private val downloadFile: DownloadFile by instance()

    suspend operator fun invoke(
        store: Store,
        currentStatus: InstallationStatus
    ): Flow<ILangString> = when (currentStatus) {
        InstallationStatus.Installed -> uninstall(store)
        InstallationStatus.NotInstalled -> install(store)
    }

    private suspend fun install(store: Store) = channelFlow {
        downloadAndCacheKoaloader()
    }

    private suspend fun uninstall(store: Store) = channelFlow<ILangString> {

    }

    private suspend fun ProducerScope<ILangString>.downloadAndCacheKoaloader() {
        try {
            send(LangString { fetchingLoaderInfo })

            val releases = httpClient.get("https://api.github.com/repos/acidicoala/Koaloader/releases")
                .body<List<GitHubRelease>>()

            val release = releases
                .sortedByDescending { it.version }
                .find { it.version?.major == 3 }
                ?: throw Exception("Failed to find latest supported Koaloader release in GitHub")

            val asset = release.assets.first()

            val assetPath = paths.cache / asset.name

            if (Files.exists(assetPath) && asset.size == withContext(Dispatchers.IO) { Files.size(assetPath) }) {
                val version = release.version?.versionString
                logger.debug("Latest supported Koaloader version $version is already cached")
                return
            }

            downloadFile(url = asset.browserDownloadUrl, path = assetPath).collect { progress ->
                send(
                    LangString(
                        "%1" to progress.current,
                        "%2" to progress.total
                    ) {
                        downloadingLoaderRelease
                    }
                )
            }

            logger.debug("Finished downloading ${asset.name} to $assetPath")
        } catch (e: Exception) {
            logger.error(e, "Failed to fetch and cache Koaloader releases info")
        }
    }
}