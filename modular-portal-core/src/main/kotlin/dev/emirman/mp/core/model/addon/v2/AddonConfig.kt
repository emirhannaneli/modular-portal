package dev.emirman.mp.core.model.addon.v2

import org.yaml.snakeyaml.Yaml
import java.io.InputStream

data class AddonConfig(
    val file: String,
    val name: String,
    val version: String,
    val description: String,
    val basePath: String = "/",
    val runCommand: String,
    val libs: List<String> = listOf(),
    var mappings: List<AddonRequestMapping> = listOf(),
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun load(content: InputStream?): AddonConfig {
            if (content == null) throw NullPointerException("content")
            val string = content.readAllBytes().decodeToString()
            val yaml = Yaml()
            val map = yaml.load<Map<String, Any?>>(string)
            return AddonConfig(
                map["file"] as String,
                map["name"] as String,
                map["version"] as String,
                map["description"] as String,
                map["base-path"] as String? ?: "/",
                map["run-command"] as String,
                map["libs"] as List<String>? ?: listOf(),
                map["mappings"] as List<AddonRequestMapping>? ?: listOf(),
            )
        }
    }
}
