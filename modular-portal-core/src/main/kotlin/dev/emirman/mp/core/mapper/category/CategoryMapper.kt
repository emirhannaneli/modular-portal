package dev.emirman.mp.core.mapper.category

import dev.emirman.mp.core.dto.category.read.RBCategory
import dev.emirman.mp.core.dto.category.read.RCategory
import dev.emirman.mp.core.dto.category.update.UCategory
import dev.emirman.mp.core.exception.category.InfinityCategoryDetected
import dev.emirman.mp.core.model.category.Category
import dev.emirman.mp.core.service.category.CategoryService
import dev.emirman.mp.core.spec.category.CategorySpec
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface CategoryMapper : BaseMapper<Category, RCategory, RBCategory, UCategory> {
    override fun map(source: UCategory, destination: Category) {
        source.title?.let { destination.title = it }
        source.description?.let { destination.description = it }
        source.childs?.let { safeChildMapper(destination, it) }
    }

    override fun map(source: Category): RCategory {
        return RCategory().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            description = source.description
            childs = rbMap(gatherChilds(source.id.toString()))
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: Category): RBCategory {
        val spec = CategorySpec()
        spec.params.parentId = source.id.toString()
        return RBCategory().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            haveChilds = service().exists(spec)
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

        fun gatherChilds(parent: String): Collection<Category> {
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


        private fun service(): CategoryService = AppContextUtil.bean(CategoryService::class.java)
    }
}