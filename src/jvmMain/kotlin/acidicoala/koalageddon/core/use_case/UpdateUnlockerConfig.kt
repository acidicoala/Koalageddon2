package acidicoala.koalageddon.core.use_case

import acidicoala.koalageddon.core.model.AppPaths
import acidicoala.koalageddon.core.model.KoalaTool
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

class UpdateUnlockerConfig(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()

    operator fun invoke(config: KoalaTool.IConfig, unlocker: KoalaTool) {
        unlocker.writeConfig(path = paths.getUnlockerConfig(unlocker), config)
    }
}