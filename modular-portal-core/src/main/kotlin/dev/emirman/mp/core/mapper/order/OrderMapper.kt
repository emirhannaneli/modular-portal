package dev.emirman.mp.core.mapper.order

import dev.emirman.mp.core.dto.order.read.RBOrder
import dev.emirman.mp.core.dto.order.read.ROrder
import dev.emirman.mp.core.dto.order.update.UOrder
import dev.emirman.mp.core.dto.user.read.RBUser
import dev.emirman.mp.core.model.order.Order
import dev.emirman.mp.core.service.order.OrderItemService
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface OrderMapper : BaseMapper<Order, ROrder, RBOrder, UOrder> {
    override fun map(source: Order): ROrder {
        return ROrder().apply {
            user = source.user
            status = source.status.toConstant()
            amount = source.amount
            type = source.type.toConstant()
            checkedOut = source.checkedOut
        }
    }

    override fun rbMap(source: Order): RBOrder {
        return RBOrder().apply {
            user = RBUser().apply {
                id = source.user.id
                sk = source.user.sk
                email = source.user.email
                createdAt = source.user.createdAt
                updatedAt = source.user.updatedAt
            }
            type = source.type.toConstant()
            status = source.status.toConstant()
            amount = source.amount
            checkedOut = source.checkedOut
        }
    }

    private fun itemService(): OrderItemService = AppContextUtil.bean(OrderItemService::class.java)
}