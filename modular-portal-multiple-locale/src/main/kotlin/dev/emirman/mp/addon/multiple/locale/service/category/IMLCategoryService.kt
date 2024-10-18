package dev.emirman.mp.addon.multiple.locale.service.category

import dev.emirman.mp.addon.multiple.locale.dto.category.create.CMLCategory
import dev.emirman.mp.addon.multiple.locale.dto.category.update.UMLCategory
import dev.emirman.mp.addon.multiple.locale.model.category.MLCategory
import dev.emirman.mp.addon.multiple.locale.repo.category.MLCategoryRepo
import dev.emirman.mp.addon.multiple.locale.service.translator.TranslatorService
import dev.emirman.mp.addon.multiple.locale.spec.category.MLCategorySpec
import dev.emirman.mp.core.service.category.CategoryService
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

@Service
class IMLCategoryService(
    val repo: MLCategoryRepo,
    val service: CategoryService,
    val translator: TranslatorService
) : MLCategoryService {
    override fun sync(id: String) {
        val category = service.findById(id)
        val spec = MLCategorySpec()
        spec.params.categoryId = id

        val query = spec.ofSearch()
        val mlCategories = repo.findAll(query)

        for (mlCategory in mlCategories) {
            mlCategory.title = translator.translate(category.title, mlCategory.locale)
            mlCategory.description = translator.translate(category.description, mlCategory.locale)
            save(mlCategory)
        }
    }

    override fun generate(id: String, locales: HashSet<String>) {
        val category = service.findById(id)

        for (s in locales) {
            val locale = Locale.forLanguageTag(s)
            if (locale.language == "") continue
            val mlCategory = MLCategory(
                locale = locale,
                category = category
            )
            mlCategory.title = translator.translate(category.title, locale)
            mlCategory.description = translator.translate(category.description, locale)

            save(mlCategory)
        }
    }

    override fun create(create: CMLCategory): MLCategory {
        val category = service.create(create)

        lateinit var preview: MLCategory

        for (s in create.locales) {
            val locale = Locale.forLanguageTag(s)
            if (locale.language == "") continue
            val mlCategory = MLCategory(
                locale = locale,
                category = category
            )
            mlCategory.title = translator.translate(category.title, locale)
            mlCategory.description = translator.translate(category.description, locale)

            preview = save(mlCategory)
        }
        return preview
    }

    override fun delete(base: MLCategory) {
        base.deleted = true
        save(base)
    }

    override fun exists(spec: MLCategorySpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: MLCategorySpec): MLCategory {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { throw NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: MLCategorySpec): Page<MLCategory> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): MLCategory {
        val spec = MLCategorySpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: MLCategory): MLCategory {
        return repo.save(base)
    }

    override fun update(base: MLCategory, update: UMLCategory) {
        map(update, base)
        service.save(base)
    }

    override fun archive(base: MLCategory) {
        base.archived = true
        save(base)
    }

    override fun clearRecycleBin() {
        val spec = MLCategorySpec()
        spec.params.deleted = true
        val query = spec.ofSearch()
        repo.delete(query)
    }

    override fun deletePermanently(id: String) {
        val spec = MLCategorySpec()
        spec.params.id = id
        val query = spec.ofSearch()
        repo.delete(query)
    }

    override fun findAllArchived(spec: MLCategorySpec): Page<MLCategory> {
        spec.params.archived = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun recycleBin(spec: MLCategorySpec): Page<MLCategory> {
        spec.params.deleted = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun restore(id: String) {
        val spec = MLCategorySpec()
        spec.params.id = id
        val mlCategory = find(spec)
        mlCategory.deleted = false
        save(mlCategory)
    }

    override fun unarchive(id: String) {
        val spec = MLCategorySpec()
        spec.params.id = id
        spec.params.archived = true
        val mlCategory = find(spec)
        mlCategory.archived = false
        save(mlCategory)
    }
}