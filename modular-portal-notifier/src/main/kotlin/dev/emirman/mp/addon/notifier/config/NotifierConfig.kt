package dev.emirman.mp.addon.notifier.config

import dev.emirman.mp.addon.notifier.model.Settings
import dev.emirman.mp.core.addon.v1.AddonLoader
import java.io.File


class NotifierConfig {
    companion object {
        private lateinit var settings: Settings
        fun load() {
            val addons = File("addons", "notifier")
            val settingsFile = File(addons, "settings.yml")

            settings = AddonLoader.loadYaml(settingsFile, Settings::class.java)
        }

        fun settings(): Settings {
            return settings
        }
    }
}