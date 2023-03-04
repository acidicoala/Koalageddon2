package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.io.appJson
import acidicoala.koalageddon.core.io.reInitKtor
import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.Settings
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class HttpClientUtil(override val di: DI) : DIAware {
    private val logger: AppLogger by instance()
    val settings: MutableStateFlow<Settings> by instance()
    operator fun invoke() {
        if (settings.value.enableProxy){
            //Not elegant, but effective.
            reInitKtor(HttpClient{
                expectSuccess = true
                install(HttpTimeout)
                install(ContentNegotiation) {
                    json(appJson)
                }
                engine {
                    proxy = ProxyBuilder.http(settings.value.proxy)
                }
            })
            logger.info("EnableProxy: true. Setting proxy to ${settings.value.proxy}")
        }else{
            reInitKtor(HttpClient{
                expectSuccess = true
                install(HttpTimeout)
                install(ContentNegotiation) {
                    json(appJson)
                }
                engine {
                    proxy = null
                }
            })
            logger.info("EnableProxy: false. Setting proxy to NO_PROXY")
        }

    }
}