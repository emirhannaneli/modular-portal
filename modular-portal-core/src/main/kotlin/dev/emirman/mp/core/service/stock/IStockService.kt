package dev.emirman.mp.core.service.stock

import dev.emirman.mp.core.dto.stock.create.CStock
import dev.emirman.mp.core.dto.stock.update.UStock
import dev.emirman.mp.core.event.stock.StockEventPublisher
import dev.emirman.mp.core.model.stock.Stock
import dev.emirman.mp.core.repo.jpa.stock.StockRepo
import dev.emirman.mp.core.service.product.ProductService
import dev.emirman.mp.core.spec.stock.StockSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class IStockService(val repo: StockRepo, val service: ProductService, val publisher: StockEventPublisher) : StockService {
    override fun moveToBlocked(stock: Stock, amount: BigDecimal) {
        stock.amount = stock.amount.subtract(amount)
        stock.blocked = stock.blocked.add(amount)
        save(stock)
    }

    override fun pullFromBlocked(stock: Stock, amount: BigDecimal) {
        stock.amount = stock.amount.add(amount)
        stock.blocked = stock.blocked.subtract(amount)
        save(stock)
    }

    override fun create(create: CStock): Stock {
        val product = service.findById(create.product)
        val stock = Stock(
            product = product,
            amount = create.amount
        )
        stock.nonStock = create.nonStock

        val saved = save(stock)

        publisher.publishNewStockEvent(saved)

        return saved
    }

    override fun delete(base: Stock) {
        base.deleted = true
        publisher.publishStockDeletedEvent(base)
        save(base)
    }

    override fun exists(spec: StockSpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: StockSpec): Stock {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: StockSpec): Page<Stock> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): Stock {
        val spec = StockSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: Stock): Stock {
        return repo.save(base)
    }

    override fun update(base: Stock, update: UStock) {
        map(update, base)
        save(base)
        publisher.publishStockUpdatedEvent(base)
    }
}