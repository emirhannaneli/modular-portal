package dev.emirman.mp.core.controller.v1.category

import dev.emirman.mp.core.dto.category.create.CCategory
import dev.emirman.mp.core.dto.category.read.RCategory
import dev.emirman.mp.core.dto.category.update.UCategory
import dev.emirman.mp.core.model.category.Category
import dev.emirman.mp.core.service.category.CategoryService
import dev.emirman.mp.core.spec.category.CategorySpec
import net.lubble.util.PageResponse
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("v1/categories")
class CategoryController(val service: CategoryService) : BaseController<CCategory, UCategory, RCategory, Category.Params> {
    override fun create(create: CCategory): ResponseEntity<RCategory> {
        val category = service.create(create)
        val rCategory = service.map(category)
        return ResponseEntity
            .created(URI.create("/v1/categories/${rCategory.sk}"))
            .body(rCategory)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val category = service.findById(id)
        service.delete(category)
        return Response(
            message = "category.messages.delete.success",
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RCategory> {
        val category = service.findById(id)
        val rCategory = service.map(category)

        return ResponseEntity.ok(rCategory)
    }

    override fun update(id: String, update: UCategory): ResponseEntity<RCategory> {
        val category = service.findById(id)
        service.update(category, update)
        val rCategory = service.map(category)

        return ResponseEntity.ok(rCategory)
    }

    override fun findAll(@ParameterObject params: Category.Params): ResponseEntity<PageResponse> {
        val spec = CategorySpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val categories = service.findAll(spec)
        val rbCategories = service.rbMap(categories.content)

        return Response.ofPage(
            page = categories,
            data = rbCategories
        ).build()
    }

    override fun archive(id: String): ResponseEntity<Response> {
        val category = service.findById(id)
        service.archive(category)
        return Response(
            message = "category.messages.archive.success",
        ).build()
    }

    override fun clearRecycleBin(): ResponseEntity<Response> {
        service.clearRecycleBin()
        return Response(
            message = "category.messages.clearRecycleBin.success",
        ).build()
    }

    override fun deletePermanently(id: String): ResponseEntity<Response> {
        service.deletePermanently(id)
        return Response(
            message = "category.messages.deletePermanently.success",
        ).build()
    }

    override fun findAllArchived(@ParameterObject params: Category.Params): ResponseEntity<PageResponse> {
        val spec = CategorySpec(params)
        val categories = service.findAllArchived(spec)
        val rbCategories = service.rbMap(categories.content)

        return Response.ofPage(
            page = categories,
            data = rbCategories
        ).build()
    }

    override fun recycleBin(@ParameterObject params: Category.Params): ResponseEntity<PageResponse> {
        val spec = CategorySpec(params)
        val categories = service.recycleBin(spec)
        val rbCategories = service.rbMap(categories.content)

        return Response.ofPage(
            page = categories,
            data = rbCategories
        ).build()
    }

    override fun restore(id: String): ResponseEntity<Response> {
        service.restore(id)
        return Response(
            message = "category.messages.restore.success",
        ).build()
    }

    override fun unarchive(id: String): ResponseEntity<Response> {
        service.unarchive(id)
        return Response(
            message = "category.messages.unarchive.success",
        ).build()
    }
}