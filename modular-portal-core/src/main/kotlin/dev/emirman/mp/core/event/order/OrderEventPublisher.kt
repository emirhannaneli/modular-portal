package dev.emirman.mp.core.event.order

import dev.emirman.mp.core.model.order.Order
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(private val publisher: ApplicationEventPublisher) {
    fun publishNewOrder(order: Order) {
        publisher.publishEvent(NewOrderEvent(order))
    }

    fun publishAcceptOrder(order: Order) {
        publisher.publishEvent(AcceptOrderEvent(order))
    }

    fun publishRejectOrder(order: Order) {
        publisher.publishEvent(RejectOrderEvent(order))
    }

    fun publishCancelOrder(order: Order) {
        publisher.publishEvent(CancelOrderEvent(order))
    }

    fun publishDeliverOrder(order: Order) {
        publisher.publishEvent(DeliverOrderEvent(order))
    }

    fun publishCompleteOrder(order: Order) {
        publisher.publishEvent(CompleteOrderEvent(order))
    }

    fun publishRefundOrder(order: Order) {
        publisher.publishEvent(RefundOrderEvent(order))
    }
}