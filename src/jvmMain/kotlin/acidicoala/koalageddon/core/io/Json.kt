package acidicoala.koalageddon.core.io

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val appJson = Json {
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
    ignoreUnknownKeys = true
    prettyPrintIndent = "  "
}