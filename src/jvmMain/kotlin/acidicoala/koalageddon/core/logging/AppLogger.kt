package acidicoala.koalageddon.core.logging

import acidicoala.koalageddon.BuildConfig
import acidicoala.koalageddon.core.model.AppPaths
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.tinylog.configuration.Configuration
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.util.stream.Collectors

/**
 * This class decouples the main codebase from logger implementation.
 */
@Suppress("unused")
class AppLogger(override val di: DI) : DIAware {
    private val paths: AppPaths by instance()

    private val logger: Logger

    private val userRegex = """\w:[/\\]Users[/\\]([^/\\]+)""".toRegex(RegexOption.IGNORE_CASE)

    init {

        val format = "{message}"

        Configuration.replace(
            mapOf(
                "level" to "trace",
                "writerConsole" to "console",
                "writerConsole.format" to format,

                "writerFile" to "file",
                "writerFile.format" to format,
                "writerFile.file" to paths.log.toString(),
                "writerFile.charset" to "UTF-8",
            )
        )

        logger = LoggerFactory.getLogger(AppLogger::class.java)

        info("🐨💥 Koalageddon v${BuildConfig.APP_VERSION}")
    }

    private fun format(levelEmoji: String, message: String): String {
        // Remove usernames from log messages in order to ensure user's privacy
        val userMatch = userRegex.find(message)
        val userName = userMatch?.groups?.get(1)?.value

        val sanitisedMessage = userName?.let { message.replace(it, "%USERNAME%") }
            ?: message

        val time = SimpleDateFormat("HH:mm:ss.SSS").format(Date.from(Instant.now()))

        val lineSource = StackWalker.getInstance()
            .walk { it.collect(Collectors.toList()) }
            .getOrNull(2)
            ?.run {
                val line = "$lineNumber".padStart(3)
                val source = fileName.padEnd(24)

                "$line:$source"
            }

        return "$levelEmoji│ $time │ $lineSource ┃ $sanitisedMessage"
    }

    fun trace(message: String) = logger.trace(format("🟦", message))
    fun debug(message: String) = logger.debug(format("⬛", message))
    fun info(message: String) = logger.info(format("🟩", message))
    fun warn(message: String) = logger.warn(format("🟨", message))
    fun error(message: String) = logger.error(format("🟥", message))
    fun error(t: Throwable, message: String) = logger.error(format("🟥", message), t)
}