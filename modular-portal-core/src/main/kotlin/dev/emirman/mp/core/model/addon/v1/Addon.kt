package dev.emirman.mp.core.model.addon.v1

import dev.emirman.mp.core.addon.v1.AddonLoader
import org.springframework.context.ConfigurableApplicationContext
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.net.URLClassLoader

class Addon(
    val file: File,
    val context: ConfigurableApplicationContext,
    private var loader: AddonLoader? = null,
) {
    val config: AddonConfig

    init {
        val url = file.toURI().toURL()
        val loader = URLClassLoader(arrayOf(url))
        val inputStream = loader.getResourceAsStream("config.yml") ?: throw Exception("config.yml not found")
        val yaml = Yaml().load(inputStream) as Map<String, Any>
        val main = yaml["main"] as String
        val name = yaml["name"] as String
        val version = yaml["version"] as String
        val author = yaml["author"] as String?
        val description = yaml["description"] as String?
        val depends = yaml["depends"] as List<String>?
        val softDepends = yaml["soft-depends"] as List<String>?
        config = AddonConfig(main, name, version, author, description, depends, softDepends)
    }

    fun loader(): AddonLoader {
        if (loader == null) {
            loader = AddonLoader.load(context, this)
        }
        return loader!!
    }

    fun loaded(): Boolean {
        return loader != null
    }

    fun info() {
        println("Addon: ${config.name} v${config.version}")
        println("Author: ${config.author}")
        println("Description: ${config.description}")

        println("Main: ${loader?.main?.name}")
        println("Configurations: ${loader?.configurations?.size}")
        println("Components: ${loader?.components?.size}")
        println("Services: ${loader?.services?.size}")
        println("Controllers: ${loader?.controllers?.size}")
    }
}