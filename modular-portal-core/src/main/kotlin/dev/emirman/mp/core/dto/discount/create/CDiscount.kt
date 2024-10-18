package dev.emirman.mp.core.dto.discount.create

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.Date

class CDiscount {
    @NotNull
    lateinit var amount: BigDecimal
    var products: Set<String>? = hashSetOf()
    var users: Set<String>? = hashSetOf()
    var allProducts: Boolean? = false
    var allUsers: Boolean? = false
    var startDate: Date? = null
    var endDate: Date? = null
    var always: Boolean? = false
    var active: Boolean? = false
}