package dev.emirman.mp.core.dto.address.read

import net.lubble.util.constant.EnumConstant
import net.lubble.util.dto.RBase

data class RAddress(
    val title: String,
    val name: String?,
    val surname: String?,
    val country: EnumConstant,
    val city: String,
    val district: String,
    val address: String?,
    val postal: String?,
    val phone: String
) : RBase()
