package dev.emirman.mp.core.dto.order.create

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

class COrder {
    @NotBlank
    lateinit var user: String

    @NotEmpty
    lateinit var items: List<COrderItem>

    lateinit var type: String

    var checkedOut: Boolean = false
}