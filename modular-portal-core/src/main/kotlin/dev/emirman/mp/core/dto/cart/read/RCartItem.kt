package dev.emirman.mp.core.dto.cart.read

import dev.emirman.mp.core.dto.product.read.RBProduct
import java.math.BigDecimal

data class RCartItem(
    val product: RBProduct,
    var quantity: BigDecimal
)