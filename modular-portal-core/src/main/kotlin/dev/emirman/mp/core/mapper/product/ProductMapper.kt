package dev.emirman.mp.core.mapper.product

import dev.emirman.mp.core.dto.product.read.RBProduct
import dev.emirman.mp.core.dto.product.read.RProduct
import dev.emirman.mp.core.dto.product.update.UProduct
import dev.emirman.mp.core.model.discount.Discount
import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.content.ContentService
import dev.emirman.mp.core.service.discount.DiscountService
import dev.emirman.mp.core.spec.discount.DiscountSpec
import net.lubble.util.AppContextUtil
import net.lubble.util.AuthTool
import net.lubble.util.mapper.BaseMapper
import java.math.BigDecimal
import java.math.RoundingMode

interface ProductMapper : BaseMapper<Product, RProduct, RBProduct, UProduct> {
    override fun map(source: UProduct, destination: Product) {
        source.title?.let { destination.title = it }
        source.price?.let { destination.price = it }
        source.description?.let { destination.description = it }
        source.images?.let { destination.images(it) }
    }

    override fun map(source: Product): RProduct {
        return RProduct().apply {
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
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    fun mapWithDiscount(source: Product): RProduct {
        val rProduct = map(source)

        val discount = calcDiscount(source) ?: return rProduct

        rProduct.discount = true
        rProduct.discountRate = discount.setScale(0, RoundingMode.UP).toInt()
        rProduct.discountedPrice = calcDiscountedPrice(source, discount)

        return rProduct
    }

    fun mapWithDiscount(source: Collection<Product>): List<RProduct> {
        return source.map { mapWithDiscount(it) }
    }

    override fun rbMap(source: Product): RBProduct {
        return RBProduct().apply {
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

    fun rbMapWithDiscount(source: Product): RBProduct {
        val rbProduct = rbMap(source)

        val discount = calcDiscount(source) ?: return rbProduct

        rbProduct.discount = true
        rbProduct.discountRate = discount.setScale(0, RoundingMode.UP).toInt()
        rbProduct.discountedPrice = calcDiscountedPrice(source, discount)

        return rbProduct
    }

    fun rbMapWithDiscount(source: Collection<Product>): List<RBProduct> {
        return source.map { rbMapWithDiscount(it) }
    }

    fun calcDiscount(source: Product): BigDecimal? {
        val discounts: HashSet<Discount> = hashSetOf()

        val spec = DiscountSpec()
        spec.params.deleted = false
        spec.params.archived = false
        spec.params.active = true

        if (!discountService().exists(spec)) return null

        var allDiscounts = discountService().findAll(spec)
        for (i in 0 until allDiscounts.totalPages) {
            discounts.addAll(allDiscounts.content)

            spec.params.page = i + 1
            allDiscounts = discountService().findAll(spec)
        }

        val productDiscounts = discounts.filter { it.products.contains(source) || it.allProducts }
            .distinctBy { it.pk }.toMutableSet()
        val userDiscounts = discounts.filter {
            val isAuth = AuthTool.isAuthenticated()
            if (isAuth) {
                val user = AuthTool.principal<User>()!!
                return@filter it.users.contains(user) || it.allUsers
            }
            it.allUsers
        }.distinctBy { it.pk }.toMutableSet()

        val availableDiscounts = hashSetOf(*productDiscounts.intersect(userDiscounts).toTypedArray())

        val toUse = availableDiscounts.maxByOrNull { it.amount } ?: return null

        return toUse.amount
    }

    fun calcDiscountedPrice(source: Product, amount: BigDecimal): BigDecimal = (source.price - ((source.price / BigDecimal(100)) * amount))


    private fun contentService(): ContentService = AppContextUtil.bean(ContentService::class.java)

    private fun discountService(): DiscountService = AppContextUtil.bean(DiscountService::class.java)
}