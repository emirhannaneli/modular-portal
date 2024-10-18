package dev.emirman.mp.addon.stock.tracking.config

import dev.emirman.mp.addon.stock.tracking.Addon
import dev.emirman.mp.addon.stock.tracking.model.Settings
import dev.emirman.mp.core.addon.v1.AddonLoader
import java.io.File
import java.net.URLClassLoader

class StockTrackingConfig {
    companion object {
        lateinit var settings: Settings

        fun loadFiles() {
            val file = File("addons", "stock-tracking")
            if (!file.exists()) file.mkdir()

            val settings = File(file, "settings.yml")
            if (!settings.exists()) settings.createNewFile() else {
                Companion.settings = AddonLoader.loadYaml(settings, Settings::class.java)
                return
            }
            val loader = URLClassLoader(arrayOf(Addon::class.java.protectionDomain.codeSource.location.toURI().toURL()))
            val inputStream = loader.getResourceAsStream("settings.yml")
            val properties = inputStream?.use { it.reader().readText() } ?: ""
            settings.writeText(properties)
            Companion.settings = AddonLoader.loadYaml(settings, Settings::class.java)
        }
    }
}