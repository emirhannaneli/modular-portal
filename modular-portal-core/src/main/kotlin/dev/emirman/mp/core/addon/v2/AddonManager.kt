package dev.emirman.mp.core.addon.v2

import dev.emirman.mp.core.model.addon.v2.Addon
import org.springframework.context.annotation.Configuration
import java.io.File


@Configuration("AddonManagerV2")
class AddonManager {
    companion object {
        private val addons: HashSet<Addon> = hashSetOf()
        private fun addons(): HashSet<Addon> {
            return addons
        }

        private fun loadAddon(file: File): Addon {
            return Addon(file)
        }

        private fun unloadAddon(addon: Addon) {
            addon.runtime?.stop()
        }

        private fun reloadAddon(addon: Addon) {
            unloadAddon(addon)
            loadAddon(addon.file)
        }

        private fun reload() {
            for (addon in addons()) {
                reloadAddon(addon)
            }
        }

        fun loadAddons() {
            val folder = File("configs")
            if (!folder.exists()) folder.mkdir()
            val files = folder.listFiles()?.filter { it.isFile && it.extension == "yml" }
                ?: return
            for (file in files) {
                val addon = loadAddon(file)
                addons.add(addon)
                println("Addon loaded: ${addon.config().name}")
            }
        }
    }
}

/*
@Bean
fun locator(builder: RouteLocatorBuilder): RouteLocator {
    AddonManager.loadAddons()
    val routes = builder.routes()
    var port = 10000
    for (addon in AddonManager.addons) {
        routes.route {
            it.path(addon.config().basePath + "/**")
                .filters { filter ->
                    filter.stripPrefix(1)
                }
                .uri("http://localhost:$port")
        }
        addon.inject(port)
        port++
    }
    return routes.build()
}
@PreDestroy
fun destroy() {
    for (addon in AddonManager.addons()) {
        AddonManager.unloadAddon(addon)
    }
}*/