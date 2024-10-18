package dev.emirman.mp.addon.notifier

import dev.emirman.mp.addon.notifier.config.NotifierConfig
import dev.emirman.mp.core.addon.v1.MPAddon
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader

class Addon : MPAddon {
    private val logger: Logger = LoggerFactory.getLogger(Addon::class.java)
    override fun onDisable() {}

    override fun onEnable() {
        loadFiles()
        val version = "1.0.2"
        banner(
            "Name: Notifications For Modular Portal",
            "Publisher: Emirman",
            "Author: Emirhan Naneli",
            "Version: $version",
            "Description:  This addon is for sending notifications to customers and authorities.",
            "Support: info@emirman.dev"
        )
    }

    private fun loadFiles() {
        val file = File("addons", "notifier")
        if (!file.exists()) file.mkdir()
        val settings = File(file, "settings.yml")
        if (!settings.exists()) settings.createNewFile() else {
            NotifierConfig.load()
            return
        }
        val loader = URLClassLoader(arrayOf(Addon::class.java.protectionDomain.codeSource.location.toURI().toURL()))
        val inputStream = loader.getResourceAsStream("settings.yml")
        val properties = inputStream?.use { it.reader().readText() } ?: ""
        settings.writeText(properties)
        NotifierConfig.load()
    }

    private fun banner(vararg lines: String) {
        val separator = lines.maxOf { it.length } + 2
        logger.info("=".repeat(separator + 4))
        lines.forEach { logger.info("|| $it".padEnd(separator + 2) + "||") }
        logger.info("=".repeat(separator + 4))
    }
}