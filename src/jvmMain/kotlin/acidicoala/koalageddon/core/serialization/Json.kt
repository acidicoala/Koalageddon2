package acidicoala.koalageddon.core.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
    prettyPrintIndent = "  "
}