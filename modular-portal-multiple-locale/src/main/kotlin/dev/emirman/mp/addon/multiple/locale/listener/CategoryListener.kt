package dev.emirman.mp.addon.multiple.locale.listener

import dev.emirman.mp.addon.multiple.locale.service.category.MLCategoryService
import dev.emirman.mp.addon.multiple.locale.spec.category.MLCategorySpec
import dev.emirman.mp.core.event.category.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CategoryListener(val service: MLCategoryService) {
    @EventListener(ArchiveCategoryEvent::class)
    fun onArchiveCategory(event: ArchiveCategoryEvent) {
        val spec = MLCategorySpec()
        spec.params.categoryId = event.category.id.toString()
        val mlCategories = service.findAll(spec)
        mlCategories.forEach {
            it.archived = true
            service.save(it)
        }
    }

    @EventListener(UnarchiveCategoryEvent::class)
    fun onUnarchiveCategory(event: UnarchiveCategoryEvent) {
        val spec = MLCategorySpec()
        spec.params.categoryId = event.category.id.toString()
        val mlCategories = service.findAll(spec)
        mlCategories.forEach {
            it.archived = false
            service.save(it)
        }
    }

    @EventListener(DeleteCategoryEvent::class)
    fun onDeleteCategory(event: DeleteCategoryEvent) {
        val spec = MLCategorySpec()
        spec.params.categoryId = event.category.id.toString()
        val mlCategories = service.findAll(spec)
        mlCategories.forEach {
            it.deleted = true
            service.delete(it)
        }
    }

    @EventListener(RestoreCategoryEvent::class)
    fun onRestoreCategory(event: RestoreCategoryEvent) {
        val spec = MLCategorySpec()
        spec.params.categoryId = event.category.id.toString()
        val mlCategories = service.findAll(spec)
        mlCategories.forEach {
            it.deleted = false
            service.save(it)
        }
    }

    @EventListener(DeletePermanentlyCategoryEvent::class)
    fun onDeletePermanentlyCategory(event: DeletePermanentlyCategoryEvent) {
        val spec = MLCategorySpec()
        spec.params.categoryId = event.category.id.toString()
        val mlCategories = service.findAll(spec)
        mlCategories.forEach {
            service.deletePermanently(it.id.toString())
        }
    }
}