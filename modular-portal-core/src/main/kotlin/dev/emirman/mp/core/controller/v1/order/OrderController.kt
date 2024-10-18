package dev.emirman.mp.core.controller.v1.order

import dev.emirman.mp.core.dto.order.create.COrder
import dev.emirman.mp.core.dto.order.read.ROrder
import dev.emirman.mp.core.dto.order.update.UOrder
import dev.emirman.mp.core.model.order.Order
import dev.emirman.mp.core.service.order.OrderService
import dev.emirman.mp.core.spec.order.OrderSpec
import net.lubble.util.PageResponse
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("v1/orders")
class OrderController(val service: OrderService) : BaseController<COrder, UOrder, ROrder, Order.Params> {
    override fun create(create: COrder): ResponseEntity<ROrder> {
        val order = service.create(create)
        val rOrder = service.map(order)

        return ResponseEntity
            .created(URI("/v1/orders/${order.sk}"))
            .body(rOrder)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val order = service.findById(id)
        service.delete(order)
        return Response(
            message = "order.messages.delete.success",
        ).build()
    }

    override fun findById(id: String): ResponseEntity<ROrder> {
        val order = service.findById(id)
        val rOrder = service.map(order)

        return ResponseEntity.ok(rOrder)
    }

    override fun update(id: String, update: UOrder): ResponseEntity<ROrder> {
        val order = service.findById(id)
        service.update(order, update)
        val rOrder = service.map(order)

        return ResponseEntity.ok(rOrder)
    }

    override fun findAll(@ParameterObject params: Order.Params): ResponseEntity<PageResponse> {
        val spec = OrderSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val orders = service.findAll(spec)
        val rOrders = service.rbMap(orders.content)

        return Response.ofPage(
            page = orders,
            data = rOrders
        ).build()
    }
}