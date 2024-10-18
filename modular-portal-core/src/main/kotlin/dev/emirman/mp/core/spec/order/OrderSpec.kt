package dev.emirman.mp.core.spec.order

import dev.emirman.mp.core.model.order.Order
import net.lubble.util.spec.BaseSpec
import org.springframework.data.mongodb.core.query.Query

class OrderSpec(val params: Order.Params) : BaseSpec(params), BaseSpec.MongoModel<Order> {
    constructor() : this(Order.Params())

    override fun ofSearch(): Query {
        val query = defaultQuery(params)

        query.with(ofPageable())

        return query
    }

}