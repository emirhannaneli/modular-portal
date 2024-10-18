package dev.emirman.mp.core.service.category

import dev.emirman.mp.core.dto.category.create.CCategory
import dev.emirman.mp.core.dto.category.update.UCategory
import dev.emirman.mp.core.event.category.CategoryEventPublisher
import dev.emirman.mp.core.mapper.category.CategoryMapper
import dev.emirman.mp.core.model.category.Category
import dev.emirman.mp.core.repo.jpa.category.CategoryRepo
import dev.emirman.mp.core.spec.category.CategorySpec
import jakarta.transaction.Transactional
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
@Transactional
class ICategoryService(val repo: CategoryRepo, val publisher: CategoryEventPublisher) : CategoryService {
    override fun exists(spec: CategorySpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun create(create: CCategory): Category {
        val category = Category(
            title = create.title,
            description = create.description
        )
        val saved = save(category)

        create.childs?.let {
            CategoryMapper.safeChildMapper(saved, it)
        }

        publisher.publishNewCategoryEvent(saved)

        return saved
    }

    override fun delete(base: Category) {
        base.deleted = true
        save(base)
        publisher.publishDeleteCategoryEvent(base)
    }

    override fun find(spec: CategorySpec): Category {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: CategorySpec): Page<Category> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): Category {
        val spec = CategorySpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: Category): Category {
        return repo.save(base)
    }

    override fun update(base: Category, update: UCategory) {
        map(update, base)
        save(base)
        publisher.publishUpdateCategoryEvent(base)
    }

    override fun archive(base: Category) {
        base.archived = true
        save(base)
        publisher.publishArchiveCategoryEvent(base)
    }

    override fun clearRecycleBin() {
        val spec = CategorySpec()
        spec.params.deleted = true
        val query = spec.ofSearch()
        repo.delete(query)
    }

    override fun deletePermanently(id: String) {
        val spec = CategorySpec()
        spec.params.id = id
        spec.params.deleted = true
        val category = find(spec)
        publisher.publishDeletePermanentlyCategoryEvent(category)
        repo.delete(category)
    }

    override fun findAllArchived(spec: CategorySpec): Page<Category> {
        spec.params.archived = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun recycleBin(spec: CategorySpec): Page<Category> {
        spec.params.deleted = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun restore(id: String) {
        val spec = CategorySpec()
        spec.params.id = id
        spec.params.deleted = true
        val category = find(spec)
        category.deleted = false
        save(category)
        publisher.publishRestoreCategoryEvent(category)
    }

    override fun unarchive(id: String) {
        val spec = CategorySpec()
        spec.params.id = id
        spec.params.archived = true
        val category = find(spec)
        category.archived = false
        save(category)
        publisher.publishUnarchiveCategoryEvent(category)
    }
}