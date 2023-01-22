package acidicoala.koalageddon.core.use_case

import org.kodein.di.DI
import org.kodein.di.DIAware
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class GetFormattedTimestamp(override val di: DI) : DIAware {
    operator fun invoke(timestamp: Long, locale: Locale): String = SimpleDateFormat
        .getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale)
        .format(Date.from(Instant.ofEpochMilli(timestamp)))
}