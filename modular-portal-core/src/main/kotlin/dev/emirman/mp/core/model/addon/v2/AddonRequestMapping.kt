package dev.emirman.mp.core.model.addon.v2

class AddonRequestMapping(
    val name: String,
    val path: String,
    val method: String,
    val public: Boolean = false,
    val permissions: List<String> = listOf(),
)