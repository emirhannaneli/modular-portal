package dev.emirman.mp.core.controller.v1.account

import dev.emirman.mp.core.dto.order.read.ROrder
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.order.OrderService
import dev.emirman.mp.core.spec.order.OrderSpec
import net.lubble.util.AuthTool
import net.lubble.util.PageResponse
import net.lubble.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/account/orders")
class AccountOrderController(val service: OrderService) {
    @GetMapping
    fun orders(): ResponseEntity<PageResponse> {
        val current = AuthTool.principal<User>()!!
        val spec = OrderSpec()
        spec.params.user = current.id.toString()

        val orders = service.findAll(spec)
        val rOrders = service.rbMap(orders.content)

        return Response.ofPage(
            page = orders,
            data = rOrders
        ).build()
    }

    @GetMapping("{id}")
    fun order(@PathVariable id: String): ResponseEntity<ROrder> {
        val current = AuthTool.principal<User>()!!
        val spec = OrderSpec()
        spec.params.id = id
        spec.params.user = current.id.toString()

        val order = service.find(spec)
        val rOrder = service.map(order)

        return ResponseEntity.ok(rOrder)
    }
}