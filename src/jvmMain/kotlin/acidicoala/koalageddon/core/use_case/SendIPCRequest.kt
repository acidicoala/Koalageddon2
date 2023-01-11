package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.serialization.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.io.RandomAccessFile

class SendIPCRequest(override val di: DI) : DIAware {
    companion object {
        const val BUFFER_SIZE = 32 * 1024 // 32Kb
    }

    private val logger: AppLogger by di.instance()

    @Serializable
    data class Request(
        val name: String,
        val args: JsonObject = buildJsonObject { },
    )

    @Serializable
    data class Response(
        val success: Boolean,
        val data: JsonObject = buildJsonObject { },
    )

    suspend operator fun invoke(pipeId: String, request: Request): Response {
        val pipeName = """\\.\pipe\$pipeId"""

        return withContext(Dispatchers.IO) {
            RandomAccessFile(pipeName, "rw").use { pipe ->
                // write to pipe
                val requestString = json.encodeToString(request)
                logger.debug("Sending request to ipc pipe: '$pipeName'\n$requestString")
                pipe.writeBytes(requestString)

                // read response
                val responseBuffer = ByteArray(BUFFER_SIZE)
                val bytesRead = pipe.read(responseBuffer)
                val responseString = String(
                    bytes = responseBuffer,
                    length = bytesRead,
                    charset = Charsets.UTF_8,
                    offset = 0
                )
                logger.debug("Received response from ipc pipe: '$pipeName'\n$responseString")

                json.decodeFromString(responseString)
            }
        }
    }
}