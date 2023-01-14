package acidicoala.koalageddon.core.extensions

import java.text.StringCharacterIterator

// Source: https://stackoverflow.com/a/3758880
fun Long.toHumanReadableString(): String {
    var remainingBytes = this
    if (-1000 < remainingBytes && remainingBytes < 1000) {
        return "$remainingBytes B"
    }
    val ci = StringCharacterIterator("kMGTPE")
    while (remainingBytes <= -999950 || remainingBytes >= 999950) {
        remainingBytes /= 1000
        ci.next()
    }
    return String.format("%.1f %cB", remainingBytes / 1000.0, ci.current())
}