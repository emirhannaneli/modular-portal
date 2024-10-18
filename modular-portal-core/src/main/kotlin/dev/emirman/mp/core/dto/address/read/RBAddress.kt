package dev.emirman.mp.core.dto.address.read

import net.lubble.util.dto.RBase

data class RBAddress(
    val title: String,
    val name: String?,
    val surname: String?,
    val combined: String,
) : RBase()
