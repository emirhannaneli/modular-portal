package dev.emirman.mp.core.service.order

import dev.emirman.mp.core.dto.order.create.COrderItem
import dev.emirman.mp.core.dto.order.update.UOrderItem
import dev.emirman.mp.core.model.order.Order
import dev.emirman.mp.core.model.order.OrderItem
import dev.emirman.mp.core.repo.mongo.order.OrderItemRepo
import dev.emirman.mp.core.service.product.ProductService
import dev.emirman.mp.core.service.warehouse.WarehouseService
import dev.emirman.mp.core.spec.order.OrderItemSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class IOrderItemService(
    val repo: OrderItemRepo,
    val template: MongoTemplate,
    val warehouseService: WarehouseService,
    val productService: ProductService
) :
    OrderItemService {
    override fun create(create: COrderItem, order: Order): OrderItem {
        val warehouse = warehouseService.findById(create.warehouse)
        val product = productService.findById(create.product)
        val orderItem = OrderItem(
            order = order,
            warehouse = warehouseService.map(warehouse),
            product = productService.map(product),
            quantity = create.quantity,
            discount = "create.discount"
        )
        return repo.save(orderItem)
    }

    override fun create(create: COrderItem): OrderItem {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(base: OrderItem) {
        base.deleted = true
        save(base)
    }

    override fun exists(spec: OrderItemSpec): Boolean {
        val query = spec.ofSearch()
        return template.exists(query, OrderItem::class.java)
    }

    override fun find(spec: OrderItemSpec): OrderItem {
        val query = spec.ofSearch()
        return template.findOne(query, OrderItem::class.java) ?: throw NotFoundException("params", spec.params)
    }

    override fun findAll(spec: OrderItemSpec): Page<OrderItem> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        val result = template.find(query, OrderItem::class.java)
        query.with(PageRequest.ofSize(Int.MAX_VALUE))
        val count = template.count(query, OrderItem::class.java)
        return PageImpl(result, pageable, count)
    }

    override fun findById(id: String): OrderItem {
        val spec = OrderItemSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: OrderItem): OrderItem {
        return repo.save(base)
    }

    override fun update(base: OrderItem, update: UOrderItem) {
        map(update, base)
        save(base)
    }
}