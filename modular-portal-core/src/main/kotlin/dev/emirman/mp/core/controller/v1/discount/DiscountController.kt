package dev.emirman.mp.core.controller.v1.discount

import dev.emirman.mp.core.dto.discount.create.CDiscount
import dev.emirman.mp.core.dto.discount.read.RDiscount
import dev.emirman.mp.core.dto.discount.update.UDiscount
import dev.emirman.mp.core.model.discount.Discount
import dev.emirman.mp.core.service.discount.DiscountService
import dev.emirman.mp.core.spec.discount.DiscountSpec
import net.lubble.util.PageResponse
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("v1/discounts")
class DiscountController(val service: DiscountService) : BaseController<CDiscount, UDiscount, RDiscount, Discount.Params> {
    override fun create(create: CDiscount): ResponseEntity<RDiscount> {
        val discount = service.create(create)
        val rDiscount = service.map(discount)
        return ResponseEntity
            .created(URI.create("/v1/discounts/${rDiscount.sk}"))
            .body(rDiscount)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val discount = service.findById(id)
        service.delete(discount)
        return Response(
            message = "discount.messages.delete.success",
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RDiscount> {
        val discount = service.findById(id)
        val rDiscount = service.map(discount)

        return ResponseEntity.ok(rDiscount)
    }

    override fun update(id: String, update: UDiscount): ResponseEntity<RDiscount> {
        val discount = service.findById(id)
        service.update(discount, update)
        val rDiscount = service.map(discount)

        return ResponseEntity.ok(rDiscount)
    }

    override fun findAll(@ParameterObject params: Discount.Params): ResponseEntity<PageResponse> {
        val spec = DiscountSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val discounts = service.findAll(spec)
        val rbDiscounts = service.rbMap(discounts.content)

        return Response.ofPage(
            page = discounts,
            data = rbDiscounts
        ).build()
    }

}