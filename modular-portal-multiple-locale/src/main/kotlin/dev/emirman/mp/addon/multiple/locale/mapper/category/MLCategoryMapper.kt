package dev.emirman.mp.addon.multiple.locale.mapper.category

import dev.emirman.mp.addon.multiple.locale.dto.category.read.RBMLCategory
import dev.emirman.mp.addon.multiple.locale.dto.category.read.RMLCategory
import dev.emirman.mp.addon.multiple.locale.dto.category.update.UMLCategory
import dev.emirman.mp.addon.multiple.locale.model.category.MLCategory
import dev.emirman.mp.addon.multiple.locale.service.category.MLCategoryService
import dev.emirman.mp.addon.multiple.locale.spec.category.MLCategorySpec
import dev.emirman.mp.core.exception.category.InfinityCategoryDetected
import dev.emirman.mp.core.model.category.Category
import dev.emirman.mp.core.service.category.CategoryService
import dev.emirman.mp.core.spec.category.CategorySpec
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface MLCategoryMapper : BaseMapper<MLCategory, RMLCategory, RBMLCategory, UMLCategory> {
    override fun map(source: UMLCategory, destination: MLCategory) {
        source.title?.let { destination.title = it }
        source.description?.let { destination.description = it }
    }

    override fun map(source: MLCategory): RMLCategory {
        val spec = MLCategorySpec()
        spec.params.locale = source.locale.language
        val mlChilds = gatherChilds(source.category.id.toString()).filter {
            spec.params.categoryId = it.id.toString()
            mlService().exists(spec)
        }.map {
            spec.params.categoryId = it.id.toString()
            mlService().find(spec)
        }
        return RMLCategory().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            description = source.description
            locale = source.locale.displayName
            childs = rbMap(mlChilds)
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: MLCategory): RBMLCategory {
        val spec = CategorySpec()
        spec.params.parentId = source.category.id.toString()
        return RBMLCategory().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            haveChilds = service().exists(spec) && map(source).childs.isNullOrEmpty()
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    companion object {
        fun safeChildMapper(source: Category, childs: Collection<String>) {
            if (childs.isEmpty()) {
                val collectedChilds = gatherChilds(source.id.toString())
                for (child in collectedChilds) {
                    child.parent = null
                }
            }

            val parentChilds = source.parent?.let { gatherChilds(it.id.toString()) } ?: emptyList()
            for (child in childs) {
                val category = service().findById(child)
                if (hasCycle(category, emptyList(), parentChilds)) throw InfinityCategoryDetected()
                category.parent = source
            }
        }

        private fun hasCycle(child: Category, visited: Collection<Category>, currentlyBeingVisited: Collection<Category>): Boolean {
            if (currentlyBeingVisited.any { it.matchesId(child.id.toString()) }) return true

            if (visited.any { it.matchesId(child.id.toString()) }) return false

            visited.plus(child)
            currentlyBeingVisited.plus(child)

            val childs = gatherChilds(child.id.toString())
            for (c in childs)
                if (hasCycle(c, visited, currentlyBeingVisited)) return true

            currentlyBeingVisited.minus(child)

            return false
        }

        private fun gatherChilds(parent: String): Collection<Category> {
            val childs = mutableListOf<Category>()

            val spec = CategorySpec()
            spec.params.parentId = parent
            var categories = service().findAll(spec)
            for (page in 1..categories.totalPages) {
                val result = categories.content
                childs.addAll(result)
                spec.apply {
                    params.page = page
                }
                categories = service().findAll(spec)
            }

            return childs.distinctBy { it.pk }
        }


        private fun service(): CategoryService {
            return AppContextUtil.bean(CategoryService::class.java)
        }

        private fun mlService(): MLCategoryService {
            return AppContextUtil.bean(MLCategoryService::class.java)
        }
    }
}