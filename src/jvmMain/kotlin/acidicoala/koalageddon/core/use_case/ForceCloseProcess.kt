package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.logging.AppLogger
import kotlinx.coroutines.future.await
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull
import kotlin.streams.asSequence

class ForceCloseProcess(override val di: DI) : DIAware {
    private val logger: AppLogger by instance()

    @OptIn(ExperimentalStdlibApi::class)
    suspend operator fun invoke(processPath: Path) = ProcessHandle.allProcesses()
        .filter { it.isAlive }
        .asSequence()
        .find { processHandle ->
            processHandle.info().command().getOrNull()
                ?.let { command -> Path(command) == processPath }
                ?: false
        }
        ?.let { processHandle ->
            logger.debug("""Forcibly closing process "${processHandle.info().command().getOrNull()}"""")

            processHandle.destroy()
            processHandle.onExit().await()
        }
}