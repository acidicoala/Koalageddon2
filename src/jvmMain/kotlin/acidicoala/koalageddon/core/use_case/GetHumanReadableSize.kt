package acidicoala.koalageddon.core.use_case

import org.kodein.di.DI
import org.kodein.di.DIAware
import java.text.StringCharacterIterator
import kotlin.math.abs

class GetHumanReadableSize(override val di: DI) : DIAware {
    // Source: https://stackoverflow.com/a/3758880
    operator fun invoke(bytes: Long): String {
        val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
        if (absB < 1024) {
            return "$bytes B"
        }
        var value = absB
        val characterIterator = StringCharacterIterator("KMGTPE")
        var i = 40
        while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
            value = value shr 10
            characterIterator.next()
            i -= 10
        }
        value *= java.lang.Long.signum(bytes).toLong()
        return java.lang.String.format("%.1f %cB", value / 1024.0, characterIterator.current())
    }
}