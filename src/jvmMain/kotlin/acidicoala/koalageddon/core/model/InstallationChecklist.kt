package acidicoala.koalageddon.core.model

data class InstallationChecklist(
    val koaloaderDll: Boolean? = null,
    val koaloaderConfig: Boolean? = null,
    val unlockerDll: Boolean? = null,
    val unlockerConfig: Boolean? = null,
    val unlockerVersion: String? = null,
) {
    private val allValues = listOf(koaloaderDll, koaloaderConfig, unlockerDll, unlockerConfig)

    val installationStatus = when {
        allValues.all { it == true } -> InstallationStatus.Installed(unlockerVersion ?: "")
        else -> InstallationStatus.NotInstalled
    }
}