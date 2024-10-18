package dev.emirman.mp.core.service.stock

import dev.emirman.mp.core.dto.stock.create.CStock
import dev.emirman.mp.core.dto.stock.update.UStock
import dev.emirman.mp.core.mapper.stock.StockMapper
import dev.emirman.mp.core.model.stock.Stock
import dev.emirman.mp.core.spec.stock.StockSpec
import net.lubble.util.service.BaseService
import java.math.BigDecimal

interface StockService : BaseService<Stock, CStock, UStock, StockSpec>, StockMapper {
    fun moveToBlocked(stock: Stock, amount: BigDecimal)

    fun pullFromBlocked(stock: Stock, amount: BigDecimal)
}