package dev.emirman.mp.addon.multiple.locale.mapper.product

import dev.emirman.mp.addon.multiple.locale.dto.product.read.RBMLProduct
import dev.emirman.mp.addon.multiple.locale.dto.product.read.RMLProduct
import dev.emirman.mp.addon.multiple.locale.dto.product.update.UMLProduct
import dev.emirman.mp.addon.multiple.locale.model.product.MLProduct
import dev.emirman.mp.core.service.content.ContentService
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface MLProductMapper : BaseMapper<MLProduct, RMLProduct, RBMLProduct, UMLProduct> {
    override fun map(source: MLProduct): RMLProduct {
        return RMLProduct().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            price = source.price
            description = source.description
            images = source.images()
            category = source.category?.id
            content = source.content?.let {
                val content = contentService().findById(it.toHexString())
                content.text
            }
            locale = source.locale.displayName
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }


    override fun rbMap(source: MLProduct): RBMLProduct {
        return RBMLProduct().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            price = source.price
            description = source.description
            images = source.images()
            category = source.category?.id
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    private fun contentService(): ContentService {
        return AppContextUtil.bean(ContentService::class.java)
    }
}