package acidicoala.koalageddon.settings.domain.use_case

import acidicoala.koalageddon.core.model.AppPaths
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class ClearCache(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()

    operator fun invoke() {
        paths.cacheDir.toFile().deleteRecursively()
    }
}