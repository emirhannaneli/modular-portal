package dev.emirman.mp.core.handler

import dev.emirman.mp.core.event.order.AcceptOrderEvent
import dev.emirman.mp.core.event.order.CancelOrderEvent
import dev.emirman.mp.core.event.order.CompleteOrderEvent
import dev.emirman.mp.core.event.order.RefundOrderEvent
import dev.emirman.mp.core.model.order.OrderType
import dev.emirman.mp.core.service.order.OrderItemService
import dev.emirman.mp.core.service.stock.StockService
import dev.emirman.mp.core.spec.order.OrderItemSpec
import dev.emirman.mp.core.spec.stock.StockSpec
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OrderStockHandler(val service: StockService, val itemService: OrderItemService) {

    @EventListener(AcceptOrderEvent::class)
    fun onAcceptOrder(event: AcceptOrderEvent) {
        val order = event.order

        when (order.type) {
            OrderType.SELLING -> {
                val itemSpec = OrderItemSpec()
                itemSpec.params.orderId = order.id
                var items = itemService.findAll(itemSpec)

                for (i in 0 until items.totalPages) {
                    items.forEach {
                        val spec = StockSpec()
                        spec.params.warehouseId = it.warehouse.id.toString()
                        spec.params.productId = it.product.id.toString()

                        val stock = service.find(spec)
                        service.moveToBlocked(stock, it.quantity)
                    }

                    itemSpec.params.page = i + 1
                    items = itemService.findAll(itemSpec)
                }
            }

            OrderType.BUYING -> {}
        }
    }

    @EventListener(RefundOrderEvent::class)
    fun onRefundOrder(event: RefundOrderEvent) {
        val order = event.order

        when (order.type) {
            OrderType.SELLING -> {
                val itemSpec = OrderItemSpec()
                itemSpec.params.orderId = order.id
                var items = itemService.findAll(itemSpec)

                for (i in 0 until items.totalPages) {
                    items.forEach {
                        val spec = StockSpec()
                        spec.params.warehouseId = it.warehouse.id.toString()
                        spec.params.productId = it.product.id.toString()

                        val stock = service.find(spec)
                        service.pullFromBlocked(stock, it.quantity)
                    }

                    itemSpec.params.page = i + 1
                    items = itemService.findAll(itemSpec)
                }
            }

            OrderType.BUYING -> {}
        }
    }

    @EventListener(CancelOrderEvent::class)
    fun onCancelOrder(event: CancelOrderEvent) {
        val order = event.order

        when (order.type) {
            OrderType.SELLING -> {
                val itemSpec = OrderItemSpec()
                itemSpec.params.orderId = order.id
                var items = itemService.findAll(itemSpec)

                for (i in 0 until items.totalPages) {
                    items.forEach {
                        val spec = StockSpec()
                        spec.params.warehouseId = it.warehouse.id.toString()
                        spec.params.productId = it.product.id.toString()

                        val stock = service.find(spec)
                        service.pullFromBlocked(stock, it.quantity)
                    }

                    itemSpec.params.page = i + 1
                    items = itemService.findAll(itemSpec)
                }
            }

            OrderType.BUYING -> {}
        }
    }

    @EventListener(CompleteOrderEvent::class)
    fun onCompleteOrder(event: CompleteOrderEvent) {
        val order = event.order

        when (order.type) {
            OrderType.SELLING -> {
                val itemSpec = OrderItemSpec()
                itemSpec.params.orderId = order.id
                var items = itemService.findAll(itemSpec)

                for (i in 0 until items.totalPages) {
                    items.forEach {
                        val spec = StockSpec()
                        spec.params.warehouseId = it.warehouse.id.toString()
                        spec.params.productId = it.product.id.toString()

                        val stock = service.find(spec)
                        stock.blocked = stock.blocked.subtract(it.quantity)
                        service.save(stock)
                    }

                    itemSpec.params.page = i + 1
                    items = itemService.findAll(itemSpec)
                }
            }

            OrderType.BUYING -> {
                val itemSpec = OrderItemSpec()
                itemSpec.params.orderId = order.id
                var items = itemService.findAll(itemSpec)

                for (i in 0 until items.totalPages) {
                    items.forEach {
                        val spec = StockSpec()
                        spec.params.warehouseId = it.warehouse.id.toString()
                        spec.params.productId = it.product.id.toString()

                        val stock = service.find(spec)
                        stock.amount = stock.amount.add(it.quantity)
                        service.save(stock)
                    }

                    itemSpec.params.page = i + 1
                    items = itemService.findAll(itemSpec)
                }
            }
        }
    }
}