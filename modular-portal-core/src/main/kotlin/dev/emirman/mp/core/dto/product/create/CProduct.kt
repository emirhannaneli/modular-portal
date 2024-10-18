package dev.emirman.mp.core.dto.product.create

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import net.lubble.util.ReFormat

import java.math.BigDecimal

open class CProduct {
    @NotBlank
    lateinit var title: String

    @NotNull
    @DecimalMin("0.0")
    lateinit var price: BigDecimal

    var description: String? = null

    var content: String? = null

    var images: HashSet<String>? = null

    var category: Long? = null

    fun format() {
        title = ReFormat(title).trim().format()
        description = description?.let { ReFormat(it).trim().format() }
    }
}