package acidicoala.koalageddon.core.model

class SemanticVersion private constructor(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val versionString: String,
) : Comparable<SemanticVersion> {
    companion object {
        private val versionRegex = """v(\d+)\.(\d+)\.(\d+)""".toRegex()

        fun fromGitTag(tag: String): SemanticVersion? {
            return versionRegex.find(tag)?.groups?.let { groups ->
                fun getCapture(index: Int) = groups[index]?.value?.toInt()

                SemanticVersion(
                    major = getCapture(1) ?: return null,
                    minor = getCapture(2) ?: return null,
                    patch = getCapture(3) ?: return null,
                    versionString = tag
                )
            }
        }
    }

    private val value
        get() = major * 1_000_000 + minor * 1_000 + patch

    override fun compareTo(other: SemanticVersion) = value - other.value
}