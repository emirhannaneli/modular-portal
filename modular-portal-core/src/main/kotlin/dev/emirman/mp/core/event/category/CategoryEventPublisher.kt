package dev.emirman.mp.core.event.category

import dev.emirman.mp.core.model.category.Category
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CategoryEventPublisher(val publisher: ApplicationEventPublisher) {
    fun publishNewCategoryEvent(category: Category) {
        publisher.publishEvent(NewCategoryEvent(category))
    }

    fun publishUpdateCategoryEvent(category: Category) {
        publisher.publishEvent(UpdateCategoryEvent(category))
    }

    fun publishArchiveCategoryEvent(category: Category) {
        publisher.publishEvent(ArchiveCategoryEvent(category))
    }

    fun publishUnarchiveCategoryEvent(category: Category) {
        publisher.publishEvent(UnarchiveCategoryEvent(category))
    }

    fun publishDeleteCategoryEvent(category: Category) {
        publisher.publishEvent(DeleteCategoryEvent(category))
    }

    fun publishRestoreCategoryEvent(category: Category) {
        publisher.publishEvent(RestoreCategoryEvent(category))
    }

    fun publishDeletePermanentlyCategoryEvent(category: Category) {
        publisher.publishEvent(DeletePermanentlyCategoryEvent(category))
    }
}