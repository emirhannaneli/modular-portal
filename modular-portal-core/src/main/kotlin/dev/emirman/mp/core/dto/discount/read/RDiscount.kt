package dev.emirman.mp.core.dto.discount.read

import dev.emirman.mp.core.dto.product.read.RBProduct
import dev.emirman.mp.core.dto.user.read.RBUser
import net.lubble.util.dto.RBase
import java.math.BigDecimal
import java.util.Date

class RDiscount : RBase() {
    lateinit var amount: BigDecimal
    var products: List<RBProduct>? = listOf()
    var users: List<RBUser>? = listOf()
    var allProducts: Boolean = false
    var allUsers: Boolean = false
    var startDate: Date? = null
    var endDate: Date? = null
    var always: Boolean = false
    var active: Boolean = false
}