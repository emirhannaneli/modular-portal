package dev.emirman.mp.core.dto.product.update

import java.math.BigDecimal

open class UProduct {
    val title: String? = null
    val price: BigDecimal? = null
    val description: String? = null
    val content: String? = null
    val images: HashSet<String>? = null
    val category: Long? = null
}