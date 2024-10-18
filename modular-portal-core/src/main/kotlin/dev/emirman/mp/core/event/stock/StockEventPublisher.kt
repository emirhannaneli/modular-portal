package dev.emirman.mp.core.event.stock

import dev.emirman.mp.core.model.stock.Stock
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class StockEventPublisher(val publisher: ApplicationEventPublisher) {

    fun publishNewStockEvent(stock: Stock) {
        publisher.publishEvent(NewStockEvent(stock))
    }

    fun publishStockUpdatedEvent(stock: Stock) {
        publisher.publishEvent(StockUpdatedEvent(stock))
    }

    fun publishStockDeletedEvent(stock: Stock) {
        publisher.publishEvent(StockDeletedEvent(stock))
    }
}