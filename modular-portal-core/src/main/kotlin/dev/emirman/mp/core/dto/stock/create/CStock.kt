package dev.emirman.mp.core.dto.stock.create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

class CStock {
    @NotBlank
    lateinit var product: String

    @NotNull
    lateinit var amount: BigDecimal

    var nonStock: Boolean = false
}