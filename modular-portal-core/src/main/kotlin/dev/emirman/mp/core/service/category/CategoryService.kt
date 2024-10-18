package dev.emirman.mp.core.service.category

import dev.emirman.mp.core.dto.category.create.CCategory
import dev.emirman.mp.core.dto.category.update.UCategory
import dev.emirman.mp.core.mapper.category.CategoryMapper
import dev.emirman.mp.core.model.category.Category
import dev.emirman.mp.core.spec.category.CategorySpec
import net.lubble.util.service.BaseService

interface CategoryService : BaseService<Category, CCategory, UCategory, CategorySpec>, CategoryMapper {
}