package acidicoala.koalageddon.core.model

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import java.nio.file.Path
import kotlin.io.path.Path

sealed class Store(val path: Path, val executable: String, val isa: ISA, val unlocker: Unlocker) {
    object Steam : Store(
        path = Path(getRegistryValue("""SOFTWARE\Valve\Steam""", "SteamPath")),
        executable = "Steam.exe",
        isa = ISA.X86,
        unlocker = Unlocker.SmokeAPI,
    )

    companion object {

        private fun getRegistryValue(key: String, value: String) =
            Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, key, value)
    }
}