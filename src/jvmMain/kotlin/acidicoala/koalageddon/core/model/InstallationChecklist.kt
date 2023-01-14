package acidicoala.koalageddon.core.model

data class InstallationChecklist(
    val loaderDll: Boolean? = null,
    val loaderConfig: Boolean? = null,
    val unlockerDll: Boolean? = null,
    val unlockerConfig: Boolean? = null,
) {
    private val allValues = listOf(loaderDll, loaderConfig, unlockerDll, unlockerConfig)

    val installationStatus = when {
        allValues.all { it == true } -> InstallationStatus.Installed
        else -> InstallationStatus.NotInstalled
    }
}