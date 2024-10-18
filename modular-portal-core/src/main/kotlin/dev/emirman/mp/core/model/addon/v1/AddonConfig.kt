package dev.emirman.mp.core.model.addon.v1

class AddonConfig(
    val main: String,
    val name: String,
    val version: String,
    val author: String? = null,
    val description: String? = null,
    val depends: List<String>? = null,
    val softDepends: List<String>? = null
)