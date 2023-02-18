package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import acidicoala.koalageddon.core.model.LangString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.awt.Desktop
import java.net.URI
import java.nio.file.Path

class OpenResourceLink(override val di: DI) : DIAware {
    private val logger: AppLogger by instance()
    private val showSnackbar: ShowSnackbar by instance()
    private val scope: CoroutineScope by instance()

    operator fun invoke(path: Path) {
        try {
            logger.debug("Opening file/directory in explorer: $path")

            Desktop.getDesktop().open(path.toFile())
        } catch (e: Exception) {
            logger.error(e, "Error opening file/directory in explorer")

            scope.launch {
                showSnackbar(LangString { error })
            }
        }
    }

    operator fun invoke(uri: URI) {
        try {
            logger.debug("Opening URI: $uri")

            Desktop.getDesktop().browse(uri)
        } catch (e: Exception) {
            logger.error(e, "Error opening URI")

            scope.launch {
                showSnackbar(LangString { error })
            }
        }
    }
}