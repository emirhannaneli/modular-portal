package dev.emirman.mp.core.dto.discount.read

import net.lubble.util.dto.RBase
import java.math.BigDecimal
import java.util.*

class RBDiscount : RBase() {
    lateinit var amount: BigDecimal
    var users: Int = 0
    var products: Int = 0
    var allProducts: Boolean = false
    var allUsers: Boolean = false
    var startDate: Date? = null
    var endDate: Date? = null
    var always: Boolean = false
    var active: Boolean = false
}