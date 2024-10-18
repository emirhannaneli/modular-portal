package dev.emirman.mp.addon.multiple.locale

import dev.emirman.mp.core.addon.v1.MPAddon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories("dev.emirman.mp.addon.multiple.locale.repo")
class Addon : MPAddon {
    private val logger: Logger = LoggerFactory.getLogger(Addon::class.java)
    override fun onDisable() {

    }

    override fun onEnable() {
        val version = "0.0.1-SNAPSHOT"
        banner(
            "Name: Multiple Locale For Modular Portal",
            "Publisher: Emirman",
            "Author: Emirhan Naneli",
            "Version: $version",
            "Description: This addon provides multiple locale support for the application.",
            "Support: info@emirman.dev"
        )
    }

    private fun banner(vararg lines: String) {
        val separator = lines.maxOf { it.length } + 2
        logger.info("=".repeat(separator + 4))
        lines.forEach { logger.info("|| $it".padEnd(separator + 2) + "||") }
        logger.info("=".repeat(separator + 4))
    }
}