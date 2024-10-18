package dev.emirman.mp.core.model.cart

import dev.emirman.mp.core.model.product.Product
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel
import java.math.BigDecimal

@Entity
@Table(name = "items")
open class CartItem(
    @ManyToOne
    open val cart: Cart,
    @ManyToOne
    open val product: Product,
    open var quantity: BigDecimal
) : BaseJPAModel() {
    class Params : SearchParams() {
    }
}