package dev.emirman.mp.addon.multiple.locale.service.category

import dev.emirman.mp.addon.multiple.locale.dto.category.create.CMLCategory
import dev.emirman.mp.addon.multiple.locale.dto.category.update.UMLCategory
import dev.emirman.mp.addon.multiple.locale.mapper.category.MLCategoryMapper
import dev.emirman.mp.addon.multiple.locale.model.category.MLCategory
import dev.emirman.mp.addon.multiple.locale.spec.category.MLCategorySpec
import net.lubble.util.service.BaseService

interface MLCategoryService : BaseService<MLCategory, CMLCategory, UMLCategory, MLCategorySpec>, MLCategoryMapper {
    fun sync(id: String)

    fun generate(id: String, locales: HashSet<String>)
}