package acidicoala.koalageddon.core.model

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import kotlin.io.path.Path

sealed class Store(
    registryEntry: Pair<String, String>,
    val executable: String,
    val isa: ISA,
    val unlocker: KoalaTool,
) {
    val directory = Path(
        try {
            getRegistryValue(registryEntry.first, registryEntry.second)
        } catch (e: Exception) {
            ""
        }
    )

    val installed get() = directory.toString().isNotBlank()

    object Steam : Store(
        registryEntry = """SOFTWARE\Valve\Steam""" to "InstallPath",
        executable = "Steam.exe",
        isa = ISA.X86,
        unlocker = KoalaTool.SmokeAPI,
    )

    object Epic : Store(
        registryEntry = """SOFTWARE\EpicGames\Unreal Engine""" to "INSTALLDIR",
        executable = "Steam.exe",
        isa = ISA.X86,
        unlocker = KoalaTool.SmokeAPI,
    )

    object Ubisoft : Store(
        registryEntry = """SOFTWARE\Ubisoft\Launcher""" to "InstallDir",
        executable = "Steam.exe",
        isa = ISA.X86,
        unlocker = KoalaTool.SmokeAPI,
    )

    companion object {
        private fun getRegistryValue(key: String, value: String) =
            Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, key, value, WinNT.KEY_WOW64_32KEY)
    }
}