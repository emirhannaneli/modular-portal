package dev.emirman.mp.core.controller.v1.product

import dev.emirman.mp.core.dto.product.create.CProduct
import dev.emirman.mp.core.dto.product.read.RProduct
import dev.emirman.mp.core.dto.product.update.UProduct
import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.service.product.ProductService
import dev.emirman.mp.core.spec.product.ProductSpec
import net.lubble.util.PageResponse
import net.lubble.util.RequestTool
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("v1/products")
class ProductController(val service: ProductService, val tool: RequestTool) :
    BaseController<CProduct, UProduct, RProduct, Product.Params> {
    override fun create(create: CProduct): ResponseEntity<RProduct> {
        create.format()

        val product = service.create(create)
        val rProduct = service.map(product)
        return ResponseEntity
            .created(URI("/v1/products/${product.sk}"))
            .body(rProduct)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val product = service.findById(id)
        service.delete(product)
        return Response(
            message = "product.messages.delete.success",
        ).build()
    }

    override fun findAll(@ParameterObject params: Product.Params): ResponseEntity<PageResponse> {
        val spec = ProductSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val discount = tool.hasHeader("calc-discount")

        val products = service.findAll(spec)
        val rProducts = if (discount) service.rbMapWithDiscount(products.content)
        else service.map(products.content)

        return Response.ofPage(
            page = products,
            data = rProducts
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RProduct> {
        val product = service.findById(id)

        val discount = tool.hasHeader("calc-discount")

        val rProduct = if (discount) service.mapWithDiscount(product)
        else service.map(product)

        return ResponseEntity.ok(rProduct)
    }

    override fun update(id: String, update: UProduct): ResponseEntity<RProduct> {
        val product = service.findById(id)
        service.update(product, update)
        val rProduct = service.map(product)

        return ResponseEntity.ok(rProduct)
    }

    override fun archive(id: String): ResponseEntity<Response> {
        val product = service.findById(id)
        service.archive(product)
        return Response(
            message = "product.messages.archive.success",
        ).build()
    }

    override fun clearRecycleBin(): ResponseEntity<Response> {
        service.clearRecycleBin()
        return Response(
            message = "product.messages.recycleBin.clear.success",
        ).build()
    }

    override fun deletePermanently(id: String): ResponseEntity<Response> {
        service.deletePermanently(id)
        return Response(
            message = "product.messages.deletePermanently.success",
        ).build()
    }

    override fun findAllArchived(@ParameterObject params: Product.Params): ResponseEntity<PageResponse> {
        val spec = ProductSpec(params)
        val products = service.findAllArchived(spec)
        val rProducts = service.map(products.content)

        return Response.ofPage(
            page = products,
            data = rProducts
        ).build()
    }

    override fun recycleBin(@ParameterObject params: Product.Params): ResponseEntity<PageResponse> {
        val spec = ProductSpec(params)
        val products = service.recycleBin(spec)
        val rProducts = service.map(products.content)

        return Response.ofPage(
            page = products,
            data = rProducts
        ).build()
    }

    override fun restore(id: String): ResponseEntity<Response> {
        service.restore(id)
        return Response(
            message = "product.messages.restore.success",
        ).build()
    }

    override fun unarchive(id: String): ResponseEntity<Response> {
        service.unarchive(id)
        return Response(
            message = "product.messages.unarchive.success",
        ).build()
    }
}