package dev.emirman.mp.core.mapper.order

import dev.emirman.mp.core.dto.order.read.RBOrderItem
import dev.emirman.mp.core.dto.order.read.ROrderItem
import dev.emirman.mp.core.dto.order.update.UOrderItem
import dev.emirman.mp.core.dto.product.read.RBProduct
import dev.emirman.mp.core.dto.warehouse.read.RBWarehouse
import dev.emirman.mp.core.model.order.OrderItem
import net.lubble.util.mapper.BaseMapper

interface OrderItemMapper : BaseMapper<OrderItem, ROrderItem, RBOrderItem, UOrderItem> {
    override fun map(source: OrderItem): ROrderItem {
        return ROrderItem().apply {
            id = source.id.toHexString()
            sk = source.sk.toKey()
            product = source.product
            warehouse = source.warehouse
        }
    }

    override fun rbMap(source: OrderItem): RBOrderItem {
        return RBOrderItem().apply {
            id = source.id.toHexString()
            sk = source.sk.toKey()
            product = RBProduct().apply {
                id = source.product.id
                sk = source.product.sk
                title = source.product.title
                description = source.product.description
                price = source.product.price
                createdAt = source.product.createdAt
                updatedAt = source.product.updatedAt
            }
            warehouse = RBWarehouse().apply {
                id = source.warehouse.id
                sk = source.warehouse.sk
                title = source.warehouse.title
                combined = "${source.warehouse.address} (${source.warehouse.phone}) - ${source.warehouse.email}"
                createdAt = source.warehouse.createdAt
                updatedAt = source.warehouse.updatedAt
            }
        }
    }
}