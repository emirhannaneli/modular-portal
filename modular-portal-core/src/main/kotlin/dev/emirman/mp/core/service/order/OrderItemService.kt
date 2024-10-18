package dev.emirman.mp.core.service.order

import dev.emirman.mp.core.dto.order.create.COrderItem
import dev.emirman.mp.core.dto.order.update.UOrderItem
import dev.emirman.mp.core.mapper.order.OrderItemMapper
import dev.emirman.mp.core.model.order.Order
import dev.emirman.mp.core.model.order.OrderItem
import dev.emirman.mp.core.spec.order.OrderItemSpec
import net.lubble.util.service.BaseService

interface OrderItemService : BaseService<OrderItem, COrderItem, UOrderItem, OrderItemSpec>, OrderItemMapper {
    fun create(create: COrderItem, order: Order): OrderItem
}