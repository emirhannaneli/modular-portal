package dev.emirman.mp.addon.multiple.locale.controller.product

import dev.emirman.mp.addon.multiple.locale.dto.product.create.CMLProduct
import dev.emirman.mp.addon.multiple.locale.dto.product.read.RMLProduct
import dev.emirman.mp.addon.multiple.locale.dto.product.update.UMLProduct
import dev.emirman.mp.addon.multiple.locale.model.product.MLProduct
import dev.emirman.mp.addon.multiple.locale.service.product.MLProductService
import dev.emirman.mp.addon.multiple.locale.spec.product.MLProductSpec
import jakarta.validation.Valid
import net.lubble.util.PageResponse
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("v1/ml/products")
class MLProductController(val service: MLProductService) : BaseController<CMLProduct, UMLProduct, RMLProduct, MLProduct.Params> {
    @PostMapping("{id}/sync")
    fun sync(@PathVariable id: String): ResponseEntity<Response> {
        service.sync(id)

        return Response(
            message = "product.ml.messages.sync.success",
        ).build()
    }

    @PostMapping("{id}/generate")
    fun generate(@PathVariable id: String, @RequestParam locales: HashSet<String>): ResponseEntity<Response> {
        service.generate(id, locales)

        return Response(
            message = "product.ml.messages.generate.success",
        ).build()
    }

    @GetMapping("{product-id}/locales")
    fun locales(
        @PathVariable(name = "product-id") id: String,
        @ParameterObject @Valid params: MLProduct.Params
    ): ResponseEntity<PageResponse> {
        val spec = MLProductSpec(params)
        spec.params.productId = id

        val mlProducts = service.findAll(spec)
        val rbmlProducts = service.rbMap(mlProducts.content)

        return Response.ofPage(
            page = mlProducts,
            data = rbmlProducts
        ).build()
    }

    override fun create(create: CMLProduct): ResponseEntity<RMLProduct> {
        val mlProduct = service.create(create)
        val rmlProduct = service.map(mlProduct)
        return ResponseEntity
            .created(URI("/v1/ml/products/${rmlProduct.sk}"))
            .body(rmlProduct)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val mlProduct = service.findById(id)
        service.delete(mlProduct)
        return Response(
            message = "product.ml.messages.delete.success",
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RMLProduct> {
        val mlProduct = service.findById(id)
        val rmlProduct = service.map(mlProduct)

        return ResponseEntity.ok(rmlProduct)
    }

    override fun update(id: String, update: UMLProduct): ResponseEntity<RMLProduct> {
        val mlProduct = service.findById(id)
        service.update(mlProduct, update)
        val rmlProduct = service.map(mlProduct)

        return ResponseEntity.ok(rmlProduct)
    }

    override fun findAll(@ParameterObject params: MLProduct.Params): ResponseEntity<PageResponse> {
        val spec = MLProductSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val locale = LocaleContextHolder.getLocale()
        spec.params.locale = locale.language

        val mlProducts = service.findAll(spec)
        val rmlProducts = service.map(mlProducts.content)

        return Response.ofPage(
            page = mlProducts,
            data = rmlProducts
        ).build()
    }
}