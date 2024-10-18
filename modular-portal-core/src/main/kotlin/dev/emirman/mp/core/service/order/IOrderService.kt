package dev.emirman.mp.core.service.order

import dev.emirman.mp.core.dto.order.create.COrder
import dev.emirman.mp.core.dto.order.update.UOrder
import dev.emirman.mp.core.event.order.OrderEventPublisher
import dev.emirman.mp.core.model.order.Order
import dev.emirman.mp.core.model.order.OrderStatus
import dev.emirman.mp.core.repo.mongo.order.OrderRepo
import dev.emirman.mp.core.service.user.UserService
import dev.emirman.mp.core.spec.order.OrderSpec
import net.lubble.util.exception.NotFoundException
import net.lubble.util.helper.EnumHelper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class IOrderService(
    val repo: OrderRepo,
    val template: MongoTemplate,
    val publisher: OrderEventPublisher,
    val orderItemService: OrderItemService,
    val userService: UserService
) : OrderService {
    override fun accept(order: Order) {
        order.status = OrderStatus.ACCEPTED
        val saved = save(order)
        publisher.publishAcceptOrder(saved)
    }

    override fun reject(order: Order) {
        order.status = OrderStatus.REJECTED
        val saved = save(order)
        publisher.publishRejectOrder(saved)
    }

    override fun cancel(order: Order) {
        order.status = OrderStatus.CANCELED
        val saved = save(order)
        publisher.publishCancelOrder(saved)
    }

    override fun deliver(order: Order) {
        order.status = OrderStatus.DELIVERED
        val saved = save(order)
        publisher.publishDeliverOrder(saved)
    }

    override fun complete(order: Order) {
        order.status = OrderStatus.COMPLETED
        val saved = save(order)
        publisher.publishCompleteOrder(saved)
    }

    override fun refund(order: Order) {
        order.status = OrderStatus.REFUNDED
        val saved = save(order)
        publisher.publishRefundOrder(saved)
    }

    override fun create(create: COrder): Order {
        val user = userService.findById(create.user)
        val order = Order(
            type = EnumHelper.findByName(create.type)
        )
        order.user = userService.map(user)

        order.checkedOut = create.checkedOut

        var saved = save(order)

        val amount: BigDecimal = BigDecimal.ZERO

        for (item in create.items) {
            val orderItem = orderItemService.create(item, saved)

            val product = orderItem.product
            val price = product.price
            val quantity = orderItem.quantity

            amount.add(price.multiply(quantity))
        }

        saved.amount = amount

        saved = save(saved)

        publisher.publishNewOrder(saved)

        return saved
    }

    override fun delete(base: Order) {
        base.deleted = true
        save(base)
    }

    override fun exists(spec: OrderSpec): Boolean {
        val query = spec.ofSearch()
        return template.exists(query, Order::class.java)
    }

    override fun find(spec: OrderSpec): Order {
        val query = spec.ofSearch()
        return template.findOne(query, Order::class.java) ?: throw NotFoundException("params", spec.params)
    }

    override fun findAll(spec: OrderSpec): Page<Order> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        val result = template.find(query, Order::class.java)
        query.with(spec.ofSortedPageable())
        val count = template.count(query, Order::class.java)
        return PageImpl(result, pageable, count)
    }

    override fun findById(id: String): Order {
        val spec = OrderSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: Order): Order {
        return repo.save(base)
    }

    override fun update(base: Order, update: UOrder) {
        map(update, base)
        save(base)
    }
}