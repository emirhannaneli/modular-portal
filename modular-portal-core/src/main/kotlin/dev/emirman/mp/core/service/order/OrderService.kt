package dev.emirman.mp.core.service.order

import dev.emirman.mp.core.dto.order.create.COrder
import dev.emirman.mp.core.dto.order.update.UOrder
import dev.emirman.mp.core.mapper.order.OrderMapper
import dev.emirman.mp.core.model.order.Order
import dev.emirman.mp.core.spec.order.OrderSpec
import net.lubble.util.service.BaseService

interface OrderService : BaseService<Order, COrder, UOrder, OrderSpec>, OrderMapper {
    fun accept(order: Order)

    fun reject(order: Order)

    fun cancel(order: Order)

    fun deliver(order: Order)

    fun complete(order: Order)

    fun refund(order: Order)
}