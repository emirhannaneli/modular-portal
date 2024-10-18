package dev.emirman.mp.core.dto.product.read

import net.lubble.util.dto.RBase
import java.math.BigDecimal

open class RBProduct : RBase() {
    lateinit var title: String
    lateinit var price: BigDecimal
    var discount: Boolean? = null
    var discountedPrice: BigDecimal? = null
    var discountRate: Int? = null
    var description: String? = null
    var images: HashSet<String>? = null
    var category: Long? = null
}