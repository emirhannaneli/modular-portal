package dev.emirman.mp.core.model.addon.v2

import java.io.File

class Addon(
    val file: File,
    var runtime: AddonRuntime? = null,
    private var active: Boolean = false,
) {
    fun inject(port: Int) {
        runtime = AddonRuntime(this, port)
        runtime?.start()

        println("Addon injected")
    }

    fun config(): AddonConfig {
        try {
            return AddonConfig.load(file.inputStream())
        } catch (e: Exception) {
            active = false
            throw RuntimeException("Addon config file is invalid", e)
        }
    }

    fun active(): Boolean {
        return active
    }
}