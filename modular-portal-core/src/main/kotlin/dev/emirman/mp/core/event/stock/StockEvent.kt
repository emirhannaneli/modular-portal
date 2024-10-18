package dev.emirman.mp.core.event.stock

import dev.emirman.mp.core.model.stock.Stock
import org.springframework.context.ApplicationEvent

abstract class StockEvent(stock: Stock) : ApplicationEvent(stock)

class NewStockEvent(val stock: Stock) : StockEvent(stock)

class StockUpdatedEvent(val stock: Stock) : StockEvent(stock)

class StockDeletedEvent(val stock: Stock) : StockEvent(stock)