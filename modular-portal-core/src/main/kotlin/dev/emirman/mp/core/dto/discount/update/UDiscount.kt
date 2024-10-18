package dev.emirman.mp.core.dto.discount.update

import java.math.BigDecimal
import java.util.*

class UDiscount {
    var amount: BigDecimal? = null
    var products: Set<String>? = hashSetOf()
    var users: Set<String>? = hashSetOf()
    var allProducts: Boolean? = null
    var allUsers: Boolean? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var always: Boolean? = null
    var active: Boolean? = null
}