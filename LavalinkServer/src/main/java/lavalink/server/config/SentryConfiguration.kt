package lavalink.server.config

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.filter.ThresholdFilter
import io.sentry.Sentry
import io.sentry.logback.SentryAppender
import lavalink.server.Launcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.util.*

/**
 * Created by napster on 25.04.18.
 */
@Configuration
class SentryConfiguration(sentryConfig: SentryConfigProperties) {
    init {
        if (sentryConfig.dsn.isNotEmpty()) {
            turnOn(sentryConfig.dsn, sentryConfig.tags, sentryConfig.environment)
        } else {
            turnOff()
        }
    }

    private final fun turnOn(dsn: String, tags: Map<String, String>, environment: String) {
        log.info("Turning on sentry")

        // set the git commit hash this was build on as the release
        val gitProps = Properties()
        try {
            gitProps.load(Launcher::class.java.classLoader.getResourceAsStream("git.properties"))
        } catch (e: NullPointerException) {
            log.error("Failed to load git repo information", e)
        } catch (e: IOException) {
            log.error("Failed to load git repo information", e)
        }
        val commitHash = gitProps.getProperty("git.commit.id")

        Sentry.init {options ->
            options.dsn = dsn
            if (environment.isNotBlank()) options.environment = environment
            tags.forEach { (name, value) -> options.tags[name] = value }

            if (commitHash != null && commitHash.isNotEmpty()) {
                log.info("Setting sentry release to commit hash $commitHash")
                options.release = commitHash
            } else {
                log.warn("No git commit hash found to set up sentry release")
            }
        }

        sentryLogbackAppender.start()
    }

    private final fun turnOff() {
        log.warn("Turning off sentry")
        Sentry.close()
        sentryLogbackAppender.stop()
    }

    companion object {
        private val log = LoggerFactory.getLogger(SentryConfiguration::class.java)
        private const val SENTRY_APPENDER_NAME = "SENTRY"

        @get:Synchronized
        private val sentryLogbackAppender: SentryAppender
            // programmatically creates a sentry appender
            get() {
                val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
                val root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)

                var sentryAppender = root.getAppender(SENTRY_APPENDER_NAME) as? SentryAppender
                if (sentryAppender == null) {
                    sentryAppender = SentryAppender()
                    sentryAppender.name = SENTRY_APPENDER_NAME
                    val warningsOrAboveFilter = ThresholdFilter()
                    warningsOrAboveFilter.setLevel(Level.WARN.levelStr)
                    warningsOrAboveFilter.start()
                    sentryAppender.addFilter(warningsOrAboveFilter)
                    sentryAppender.context = loggerContext
                    root.addAppender(sentryAppender)
                }

                return sentryAppender
            }
    }
}
