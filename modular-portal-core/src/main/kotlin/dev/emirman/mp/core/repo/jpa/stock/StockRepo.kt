package dev.emirman.mp.core.repo.jpa.stock

import dev.emirman.mp.core.model.stock.Stock
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface StockRepo : BaseJPARepo<Stock> {
}