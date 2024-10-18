package dev.emirman.mp.addon.multiple.locale.controller.category

import dev.emirman.mp.addon.multiple.locale.dto.category.create.CMLCategory
import dev.emirman.mp.addon.multiple.locale.dto.category.read.RMLCategory
import dev.emirman.mp.addon.multiple.locale.dto.category.update.UMLCategory
import dev.emirman.mp.addon.multiple.locale.model.category.MLCategory
import dev.emirman.mp.addon.multiple.locale.service.category.MLCategoryService
import dev.emirman.mp.addon.multiple.locale.spec.category.MLCategorySpec
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
@RequestMapping("v1/ml/categories")
class MLCategoryController(val service: MLCategoryService) : BaseController<CMLCategory, UMLCategory, RMLCategory, MLCategory.Params> {
    @PostMapping("{id}/sync")
    fun sync(@PathVariable id: String): ResponseEntity<Response> {
        service.sync(id)

        return Response(
            message = "category.ml.messages.sync.success",
        ).build()
    }

    @PostMapping("{id}/generate")
    fun generate(@PathVariable id: String, @RequestParam locales: HashSet<String>): ResponseEntity<Response> {
        service.generate(id, locales)

        return Response(
            message = "category.ml.messages.generate.success",
        ).build()
    }

    @GetMapping("{category-id}/locales")
    fun locales(
        @PathVariable(name = "category-id") id: String,
        @ParameterObject @Valid params: MLCategory.Params
    ): ResponseEntity<PageResponse> {
        val spec = MLCategorySpec(params)
        spec.params.categoryId = id

        val mlCategories = service.findAll(spec)
        val rbmlCategories = service.rbMap(mlCategories.content)

        return Response.ofPage(
            page = mlCategories,
            data = rbmlCategories
        ).build()
    }

    override fun create(create: CMLCategory): ResponseEntity<RMLCategory> {
        val mlCategory = service.create(create)
        val rmlCategory = service.map(mlCategory)
        return ResponseEntity
            .created(URI("/v1/ml/categories/${rmlCategory.sk}"))
            .body(rmlCategory)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val mlCategory = service.findById(id)
        service.delete(mlCategory)
        return Response(
            message = "category.ml.messages.delete.success",
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RMLCategory> {
        val mlCategory = service.findById(id)
        val rmlCategory = service.map(mlCategory)

        return ResponseEntity.ok(rmlCategory)
    }

    override fun update(id: String, update: UMLCategory): ResponseEntity<RMLCategory> {
        val mlCategory = service.findById(id)
        service.update(mlCategory, update)
        val rmlCategory = service.map(mlCategory)

        return ResponseEntity.ok(rmlCategory)
    }

    override fun findAll(@ParameterObject params: MLCategory.Params): ResponseEntity<PageResponse> {
        val spec = MLCategorySpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val locale = LocaleContextHolder.getLocale()
        spec.params.locale = locale.language

        val mlCategories = service.findAll(spec)
        val rmlCategories = service.map(mlCategories.content)

        return Response.ofPage(
            page = mlCategories,
            data = rmlCategories
        ).build()
    }
}