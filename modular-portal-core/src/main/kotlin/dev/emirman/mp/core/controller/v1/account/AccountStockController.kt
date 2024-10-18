package dev.emirman.mp.core.controller.v1.account

import dev.emirman.mp.core.dto.stock.create.CStock
import dev.emirman.mp.core.dto.stock.read.RStock
import dev.emirman.mp.core.model.stock.Stock
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.stock.StockService
import dev.emirman.mp.core.spec.stock.StockSpec
import net.lubble.util.AuthTool
import net.lubble.util.PageResponse
import net.lubble.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.net.URI

@RestController
@RequestMapping("v1/account/stocks")
class AccountStockController(val service: StockService) {
    @PostMapping
    fun addStock(
        @RequestParam("product-id") productId: String,
        @RequestParam("quantity") amount: BigDecimal
    ): ResponseEntity<RStock> {
        val current = AuthTool.principal<User>()!!

        val spec = StockSpec()
        spec.params.productId = productId
        spec.params.userId = current.id.toString()

        lateinit var stock: Stock

        val exists = service.exists(spec)
        if (exists) {
            stock = service.find(spec)
            stock.amount = stock.amount.add(amount)
        } else {
            val cStock = CStock()
            cStock.product = productId
            cStock.amount = amount
            stock = service.create(cStock)
            stock.user = current
        }

        val saved = service.save(stock)
        val rStock = service.map(saved)

        return ResponseEntity
            .created(URI("/v1/account/stocks/${rStock.sk}"))
            .body(rStock)
    }

    @GetMapping
    fun stocks(): ResponseEntity<PageResponse> {
        val current = AuthTool.principal<User>()!!
        val spec = StockSpec()
        spec.params.userId = current.id.toString()
        val stocks = service.findAll(spec)
        val rStocks = service.map(stocks.content)

        return Response.ofPage(
            page = stocks,
            data = rStocks
        ).build()
    }

    @GetMapping("{id}")
    fun stock(@PathVariable id: String): ResponseEntity<RStock> {
        val current = AuthTool.principal<User>()!!
        val spec = StockSpec()
        spec.params.id = id
        spec.params.userId = current.id.toString()
        val stock = service.find(spec)
        val rStock = service.map(stock)
        return ResponseEntity.ok(rStock)
    }

    @DeleteMapping("{id}")
    fun removeStock(@PathVariable id: String): ResponseEntity<Response> {
        val current = AuthTool.principal<User>()!!
        val spec = StockSpec()
        spec.params.id = id
        spec.params.userId = current.id.toString()
        val stock = service.find(spec)
        service.delete(stock)
        return Response(
            message = "account.stock.messages.remove.success"
        ).build()
    }
}