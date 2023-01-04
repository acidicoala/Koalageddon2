package acidicoala.koalageddon.core.serialization

import kotlinx.serialization.json.Json

val json = Json {
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
}