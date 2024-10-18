package dev.emirman.mp.core.controller.v1.warehouse

import dev.emirman.mp.core.dto.stock.create.CStock
import dev.emirman.mp.core.dto.stock.read.RStock
import dev.emirman.mp.core.model.stock.Stock
import dev.emirman.mp.core.service.stock.StockService
import dev.emirman.mp.core.service.warehouse.WarehouseService
import dev.emirman.mp.core.spec.stock.StockSpec
import net.lubble.util.PageResponse
import net.lubble.util.Response
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.net.URI

@RestController
@RequestMapping("v1/warehouses/{id}/stocks")
class WarehouseStockController(val stockService: StockService, val warehouseService: WarehouseService) {
    @PostMapping
    fun create(
        @PathVariable id: String,
        @RequestParam("product-id") productId: String,
        @RequestParam("quantity") amount: BigDecimal,
        @RequestParam("non-stock") nonStock: Boolean = false
    ): ResponseEntity<RStock> {
        val warehouse = warehouseService.findById(id)

        val spec = StockSpec()
        spec.params.productId = productId
        spec.params.warehouseId = id
        val exists = stockService.exists(spec)

        lateinit var stock: Stock

        if (exists) {
            stock = stockService.find(spec)
            stock.amount = stock.amount.add(amount)
        } else {
            val cStock = CStock()
            cStock.product = productId
            cStock.amount = amount
            cStock.nonStock = nonStock
            stock = stockService.create(cStock)
        }

        stock.warehouse = warehouse
        val saved = stockService.save(stock)
        val rStock = stockService.map(saved)

        return ResponseEntity
            .created(URI("/v1/warehouses/${warehouse.sk.toKey()}/stocks/${stock.sk.toKey()}"))
            .body(rStock)
    }

    @GetMapping("{stock-id}")
    fun find(
        @PathVariable id: String,
        @PathVariable("stock-id") stockId: String
    ): ResponseEntity<RStock> {
        val spec = StockSpec()
        spec.params.id = stockId
        spec.params.warehouseId = id
        val stock = stockService.find(spec)
        val rStock = stockService.map(stock)
        return ResponseEntity.ok(rStock)
    }

    @GetMapping
    fun findAll(
        @PathVariable id: String,
        @ParameterObject params: Stock.Params
    ): ResponseEntity<PageResponse> {
        val spec = StockSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        spec.params.warehouseId = id

        val stocks = stockService.findAll(spec)
        val rStocks = stockService.map(stocks.content)

        return Response.ofPage(
            page = stocks,
            data = rStocks
        ).build()
    }

    @PutMapping("{stock-id}")
    fun update(
        @PathVariable id: String,
        @PathVariable("stock-id") stockId: String,
        @RequestParam("quantity") amount: BigDecimal,
        @RequestParam("non-stock") nonStock: Boolean = false
    ): ResponseEntity<RStock> {
        val spec = StockSpec()
        spec.params.id = stockId
        spec.params.warehouseId = id
        val stock = stockService.find(spec)
        stock.amount = amount
        stock.nonStock = nonStock
        stockService.save(stock)
        val rStock = stockService.map(stock)
        return ResponseEntity.ok(rStock)
    }

    @DeleteMapping("{stock-id}")
    fun delete(
        @PathVariable id: String,
        @PathVariable("stock-id") stockId: String
    ): ResponseEntity<Response> {
        val spec = StockSpec()
        spec.params.id = stockId
        spec.params.warehouseId = id
        val stock = stockService.find(spec)
        stockService.delete(stock)
        return Response(
            message = "warehouse.stock.messages.delete.success",
        ).build()
    }
}