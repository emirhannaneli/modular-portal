package dev.emirman.mp.addon.auto.buy.order

import dev.emirman.mp.core.addon.v1.AddonLoader
import dev.emirman.mp.core.addon.v1.MPAddon
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Addon : MPAddon {
    private val logger: Logger = LoggerFactory.getLogger(Addon::class.java)
    override fun onDisable() {

    }

    override fun onEnable() {
        val version = "1.0.0-SNAPSHOT"
        AddonLoader.banner(
            logger,
            "Name: Auto Buy Order For Modular Portal",
            "Publisher: Emirman",
            "Author: Emirhan Naneli",
            "Version: $version",
            "Description: This addon provides auto buy order for dealer portal.",
            "Support: info@emirman.dev"
        )
    }
}