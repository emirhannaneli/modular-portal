package dev.emirman.mp.core.controller.v1.account

import dev.emirman.mp.core.dto.cart.read.RCart
import dev.emirman.mp.core.dto.order.create.COrder
import dev.emirman.mp.core.dto.order.create.COrderItem
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.cart.CartService
import dev.emirman.mp.core.service.order.OrderService
import net.lubble.util.AuthTool
import net.lubble.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("v1/account/cart")
class AccountCartController(val service: CartService, val orderService: OrderService) {
    @GetMapping
    fun retrieveCart(): ResponseEntity<RCart> {
        val current = AuthTool.principal<User>()!!
        val cart = service.cart(current.id.toString())
        val rCart = service.map(cart)
        return ResponseEntity.ok(rCart)
    }

    @PostMapping
    fun addCart(@RequestParam(name = "product-id") productId: String, @RequestParam quantity: BigDecimal): ResponseEntity<RCart> {
        val current = AuthTool.principal<User>()!!
        val cart = service.addProduct(current.id.toString(), productId)
        val rCart = service.map(cart)
        return ResponseEntity.ok(rCart)
    }

    @DeleteMapping
    fun removeCart(@RequestParam(name = "product-id") productId: String): ResponseEntity<RCart> {
        val current = AuthTool.principal<User>()!!
        val cart = service.removeProduct(current.id.toString(), productId)
        val rCart = service.map(cart)
        return ResponseEntity.ok(rCart)
    }

    @PutMapping
    fun updateCart(@RequestParam(name = "product-id") productId: String, @RequestParam quantity: Int): ResponseEntity<RCart> {
        val current = AuthTool.principal<User>()!!
        val cart = service.updateProductQuantity(current.id.toString(), productId, quantity)
        val rCart = service.map(cart)
        return ResponseEntity.ok(rCart)
    }

    @PostMapping("clear")
    fun clearCart(): ResponseEntity<RCart> {
        val current = AuthTool.principal<User>()!!
        val cart = service.clearCart(current.id.toString())
        val rCart = service.map(cart)
        return ResponseEntity.ok(rCart)
    }


    @PostMapping("checkout")
    fun checkout(): ResponseEntity<Response> {
        val current = AuthTool.principal<User>()!!

        val cart = service.cart(current.id.toString())

        val cOrder = COrder().apply {
            user = current.id.toString()
            items = cart.items.map {
                COrderItem().apply {
                    product = it.product.id.toString()
                    quantity = it.quantity
                }
            }
        }

        orderService.create(cOrder)

        return Response(
            message = "account.cart.checkout.success",
        ).build()
    }
}