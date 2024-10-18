package dev.emirman.mp.core.model.order

import dev.emirman.mp.core.dto.product.read.RProduct
import dev.emirman.mp.core.dto.warehouse.read.RWarehouse
import net.lubble.util.model.BaseMongoModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "order_items")
open class OrderItem(
    @DBRef(lazy = true, db = "orders")
    private val order: Order,
    val warehouse: RWarehouse,
    val product: RProduct,
    val quantity: BigDecimal,
    val discount: String,
) : BaseMongoModel() {
    open class Params : SearchParams() {
        var orderId: ObjectId? = null
        var warehouseId: String? = null
        var productId: String? = null
    }
}