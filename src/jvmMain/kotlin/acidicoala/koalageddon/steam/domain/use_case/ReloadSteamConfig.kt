package acidicoala.koalageddon.steam.domain.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.LangString
import acidicoala.koalageddon.core.io.appJson
import acidicoala.koalageddon.core.use_case.SendPipeRequest
import acidicoala.koalageddon.core.use_case.ShowSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class ReloadSteamConfig(override val di: DI) : DIAware {
    private val logger: AppLogger by instance()
    private val showSnackbar: ShowSnackbar by instance()
    private val sendPipeRequest: SendPipeRequest by instance()

    suspend operator fun invoke() {
        withContext(context = Dispatchers.IO) {
            try {
                val response = sendPipeRequest(
                    pipeId = "smokeapi.store.steam",
                    request = SendPipeRequest.Request(name = "config::reload")
                )

                if (response.success) {
                    showSnackbar(message = LangString { reloadConfigSuccess })
                } else {
                    val dataString = appJson.encodeToString(response.data)
                    logger.error(
                        "Received unsuccessful response for config reload request: $dataString"
                    )

                    showSnackbar(message = LangString { reloadConfigError })
                }
            } catch (e: Exception) {
                logger.error(e, "Uncaught exception reloading config")

                showSnackbar(message = LangString { reloadConfigError })
            }
        }
    }
}