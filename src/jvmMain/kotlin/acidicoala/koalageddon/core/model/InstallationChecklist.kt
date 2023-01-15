package acidicoala.koalageddon.core.model

data class InstallationChecklist(
    val koaloaderDll: Boolean = false,
    val koaloaderConfig: Boolean = false,
    val unlockerDll: String? = null,
    val unlockerConfig: Boolean = false,
) {
    private val allValues = listOf(koaloaderDll, koaloaderConfig, unlockerDll != null, unlockerConfig)

    val installationStatus = when {
        allValues.all { it } -> InstallationStatus.Installed(unlockerDll ?: "")
        else -> InstallationStatus.NotInstalled
    }
}