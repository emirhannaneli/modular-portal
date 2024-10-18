package dev.emirman.mp.core.model.order

import dev.emirman.mp.core.dto.user.read.RUser
import net.lubble.util.model.BaseMongoModel
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "orders")
open class Order(
    @Indexed
    val type: OrderType
) : BaseMongoModel() {
    lateinit var amount: BigDecimal

    @Indexed(partialFilter = "{'pk': 1, 'sk': 1}")
    lateinit var user: RUser

    @Indexed
    var status: OrderStatus = OrderStatus.PENDING

    @Indexed
    var checkedOut: Boolean = false

    open class Params : SearchParams() {
        var user: String? = null
        var status: OrderStatus? = null
        var type: OrderType? = null
        var checkedOut: Boolean? = null
    }
}