package dev.emirman.mp.core.dto.stock.read

import dev.emirman.mp.core.dto.product.read.RBProduct
import net.lubble.util.dto.RBase
import java.math.BigDecimal

class RBStock : RBase() {
    lateinit var product: RBProduct
    lateinit var amount: BigDecimal
    var nonStock: Boolean = false
}