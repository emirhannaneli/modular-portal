package dev.emirman.mp.core.event.category

import dev.emirman.mp.core.model.category.Category
import org.springframework.context.ApplicationEvent


abstract class CategoryEvent(category: Category) : ApplicationEvent(category)
class NewCategoryEvent(val category: Category) : CategoryEvent(category)
class UpdateCategoryEvent(val category: Category) : CategoryEvent(category)
class ArchiveCategoryEvent(val category: Category) : CategoryEvent(category)
class UnarchiveCategoryEvent(val category: Category) : CategoryEvent(category)
class DeleteCategoryEvent(val category: Category) : CategoryEvent(category)
class RestoreCategoryEvent(val category: Category) : CategoryEvent(category)
class DeletePermanentlyCategoryEvent(val category: Category) : CategoryEvent(category)