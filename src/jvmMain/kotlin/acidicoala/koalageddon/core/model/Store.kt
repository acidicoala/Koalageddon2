package acidicoala.koalageddon.core.model

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import java.nio.file.Path
import kotlin.io.path.Path

sealed class Store(val directory: Path, val executable: String, val isa: ISA, val unlocker: KoalaTool) {
    object Steam : Store(
        directory = Path(getRegistryValue("""SOFTWARE\Valve\Steam""", "SteamPath")),
        executable = "Steam.exe",
        isa = ISA.X86,
        unlocker = KoalaTool.SmokeAPI,
    )

    companion object {
        // TODO: Error checking
        private fun getRegistryValue(key: String, value: String) =
            Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, key, value)
    }
}