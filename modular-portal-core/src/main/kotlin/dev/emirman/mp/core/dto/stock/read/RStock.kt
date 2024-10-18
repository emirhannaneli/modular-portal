package dev.emirman.mp.core.dto.stock.read

import dev.emirman.mp.core.dto.product.read.RProduct
import net.lubble.util.dto.RBase
import java.math.BigDecimal

class RStock : RBase() {
    lateinit var product: RProduct
    lateinit var amount: BigDecimal
    var nonStock: Boolean = false
}