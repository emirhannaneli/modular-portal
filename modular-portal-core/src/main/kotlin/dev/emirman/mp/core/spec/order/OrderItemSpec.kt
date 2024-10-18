package dev.emirman.mp.core.spec.order

import dev.emirman.mp.core.model.order.OrderItem
import net.lubble.util.spec.BaseSpec
import org.springframework.data.mongodb.core.query.Query

class OrderItemSpec(val params: OrderItem.Params) : BaseSpec(params), BaseSpec.MongoModel<OrderItem> {
    constructor() : this(OrderItem.Params())

    override fun ofSearch(): Query {
        val query = defaultQuery(params)

        query.with(ofPageable())

        return query
    }
}