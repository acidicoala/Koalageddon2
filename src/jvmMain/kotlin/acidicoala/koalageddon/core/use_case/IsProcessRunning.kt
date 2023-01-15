package acidicoala.koalageddon.core.use_case

import org.kodein.di.DI
import org.kodein.di.DIAware
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.jvm.optionals.getOrNull

class IsProcessRunning(override val di: DI) : DIAware {
    @OptIn(ExperimentalStdlibApi::class)
    operator fun invoke(processPath: Path): Boolean = ProcessHandle.allProcesses()
        .filter { it.isAlive }
        .anyMatch { processHandle ->
            processHandle.info().command().getOrNull()
                ?.let { command -> Path(command) == processPath }
                ?: false
        }
}