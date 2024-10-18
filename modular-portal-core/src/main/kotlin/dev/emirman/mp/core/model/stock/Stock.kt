package dev.emirman.mp.core.model.stock

import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.model.warehouse.Warehouse
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel
import java.math.BigDecimal

@Entity
@Table(name = "stocks")
open class Stock(
    @ManyToOne
    open val product: Product,
    open var amount: BigDecimal
) : BaseJPAModel() {
    open var nonStock: Boolean = false

    @ManyToOne
    open var warehouse: Warehouse? = null

    @ManyToOne
    open var user: User? = null

    open var blocked: BigDecimal = BigDecimal.ZERO

    open class Params : SearchParams() {
        var productId: String? = null
        var warehouseId: String? = null
        var userId: String? = null
    }
}