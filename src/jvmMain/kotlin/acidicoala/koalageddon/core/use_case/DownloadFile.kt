package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.extensions.toHumanReadableString
import acidicoala.koalageddon.core.io.httpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.channelFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import java.nio.file.Path
import kotlin.io.path.writeBytes

class DownloadFile(override val di: DI) : DIAware {
    data class Progress(
        val current: String,
        val total: String,
    )

    suspend operator fun invoke(url: String, path: Path) = channelFlow {
        val assetBytes = httpClient.get(url) {
            timeout {
                requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            }
            onDownload { downloadedBytes, contentLength ->
                if (contentLength == 0L) {
                    return@onDownload
                }

                send(
                    Progress(
                        current = downloadedBytes.toHumanReadableString(),
                        total = contentLength.toHumanReadableString(),
                    )
                )
            }
        }.body<ByteArray>()

        path.writeBytes(assetBytes)
    }
}