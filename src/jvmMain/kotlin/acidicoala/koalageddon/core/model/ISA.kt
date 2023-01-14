package acidicoala.koalageddon.core.model

enum class ISA(val bitness: Int, val bitnessSuffix: String) {
    X86(bitness = 32, bitnessSuffix = ""),
    X86_64(bitness = 64, bitnessSuffix = "64")
}