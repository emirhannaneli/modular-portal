package dev.emirman.mp.core.dto.warehouse.create

import jakarta.validation.constraints.NotBlank
import net.lubble.util.ReFormat


class CWarehouse {
    @NotBlank
    lateinit var title: String

    var address: String? = null

    var phone: String? = null

    var email: String? = null

    fun format() {
        title = ReFormat(title).trim().format()
        address = address?.let { ReFormat(it).trim().format() }
        phone = phone?.let { ReFormat(it).trim().format() }
        email = email?.let { ReFormat(it).trim().format() }
    }
}