package dev.emirman.mp.core.event.order

import dev.emirman.mp.core.model.order.Order
import org.springframework.context.ApplicationEvent


abstract class OrderEvent(order: Order) : ApplicationEvent(order)
class NewOrderEvent(val order: Order) : OrderEvent(order)

class AcceptOrderEvent(val order: Order) : OrderEvent(order)

class RejectOrderEvent(val order: Order) : OrderEvent(order)

class CancelOrderEvent(val order: Order) : OrderEvent(order)

class DeliverOrderEvent(val order: Order) : OrderEvent(order)

class CompleteOrderEvent(val order: Order) : OrderEvent(order)

class RefundOrderEvent(val order: Order) : OrderEvent(order)