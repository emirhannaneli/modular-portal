package dev.emirman.mp.core.dto.address.create

import jakarta.validation.constraints.NotBlank
import net.lubble.util.ReFormat

open class CAddress {
    @NotBlank
    lateinit var title: String

    var name: String? = null

    var surname: String? = null

    @NotBlank
    lateinit var country: String

    @NotBlank
    lateinit var city: String

    @NotBlank
    lateinit var district: String

    var address: String? = null

    var postal: String? = null

    @NotBlank
    lateinit var phone: String

    fun format() {
        title = ReFormat(title).trim().format()
        name = name?.let { ReFormat(it).trim().format() }
        surname = surname?.let { ReFormat(it).trim().format() }
        country = ReFormat(country).trim().format()
        city = ReFormat(city).trim().format()
        district = ReFormat(district).trim().format()
        address = address?.let { ReFormat(it).trim().format() }
        postal = postal?.let { ReFormat(it).trim().format() }
        phone = ReFormat(phone).trim().format()
    }
}
