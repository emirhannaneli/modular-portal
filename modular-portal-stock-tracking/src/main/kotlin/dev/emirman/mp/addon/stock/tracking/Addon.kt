package dev.emirman.mp.addon.stock.tracking

import dev.emirman.mp.addon.stock.tracking.config.StockTrackingConfig
import dev.emirman.mp.core.addon.v1.MPAddon
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Addon : MPAddon {
    private val logger: Logger = LoggerFactory.getLogger(Addon::class.java)
    override fun onDisable() {

    }

    override fun onEnable() {
        StockTrackingConfig.loadFiles()
        val version = "1.0.0-SNAPSHOT"
        banner(
            "Name: Stock Tracking For Modular Portal",
            "Publisher: Emirman",
            "Author: Emirhan Naneli",
            "Version: $version",
            "Description: This addon provides stock tracking for dealer portal.",
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