package dev.emirman.mp.addon.multiple.locale.model.product

import dev.emirman.mp.core.model.product.Product
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.util.*

@Entity
class MLProduct() : Product("", BigDecimal.ZERO) {
    lateinit var locale: Locale

    @ManyToOne
    lateinit var product: Product

    constructor(locale: Locale, product: Product) : this() {
        this.locale = locale
        this.product = product
    }

    class Params : Product.Params() {
        var productId: String? = null
        var locale: String? = null
    }
}