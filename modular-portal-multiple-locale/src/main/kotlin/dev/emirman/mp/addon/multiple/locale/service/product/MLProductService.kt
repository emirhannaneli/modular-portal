package dev.emirman.mp.addon.multiple.locale.service.product

import dev.emirman.mp.addon.multiple.locale.dto.product.create.CMLProduct
import dev.emirman.mp.addon.multiple.locale.dto.product.update.UMLProduct
import dev.emirman.mp.addon.multiple.locale.mapper.product.MLProductMapper
import dev.emirman.mp.addon.multiple.locale.model.product.MLProduct
import dev.emirman.mp.addon.multiple.locale.spec.product.MLProductSpec
import net.lubble.util.service.BaseService

interface MLProductService : BaseService<MLProduct, CMLProduct, UMLProduct, MLProductSpec>, MLProductMapper {
    fun sync(id: String)

    fun generate(id: String, locales: HashSet<String>)
}