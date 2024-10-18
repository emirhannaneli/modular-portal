package dev.emirman.mp.core.dto.order.create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

class COrderItem {
    lateinit var warehouse: String

    @NotBlank
    lateinit var product: String

    @NotNull
    lateinit var quantity: BigDecimal
}