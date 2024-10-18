package dev.emirman.mp.core.mapper.discount

import dev.emirman.mp.core.dto.discount.read.RBDiscount
import dev.emirman.mp.core.dto.discount.read.RDiscount
import dev.emirman.mp.core.dto.discount.update.UDiscount
import dev.emirman.mp.core.model.discount.Discount
import dev.emirman.mp.core.service.product.ProductService
import dev.emirman.mp.core.service.user.UserService
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface DiscountMapper : BaseMapper<Discount, RDiscount, RBDiscount, UDiscount> {
    override fun map(source: Discount): RDiscount {
        return RDiscount().apply {
            id = source.id
            sk = source.sk.toKey()
            amount = source.amount
            users = userService().rbMap(source.users)
            products = productService().rbMap(source.products)
            allProducts = source.allProducts
            allUsers = source.allUsers
            startDate = source.startDate
            endDate = source.endDate
            always = source.always
            active = source.active
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: Discount): RBDiscount {
        return RBDiscount().apply {
            id = source.id
            sk = source.sk.toKey()
            amount = source.amount
            users = source.users.size
            products = source.products.size
            allProducts = source.allProducts
            allUsers = source.allUsers
            startDate = source.startDate
            endDate = source.endDate
            always = source.always
            active = source.active
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    private fun productService() = AppContextUtil.bean(ProductService::class.java)

    private fun userService() = AppContextUtil.bean(UserService::class.java)
}