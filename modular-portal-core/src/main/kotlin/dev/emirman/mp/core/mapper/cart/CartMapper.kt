package dev.emirman.mp.core.mapper.cart

import dev.emirman.mp.core.dto.cart.read.RBCart
import dev.emirman.mp.core.dto.cart.read.RCart
import dev.emirman.mp.core.dto.cart.read.RCartItem
import dev.emirman.mp.core.dto.cart.update.UCart
import dev.emirman.mp.core.model.cart.Cart
import dev.emirman.mp.core.service.product.ProductService
import net.lubble.util.AppContextUtil
import net.lubble.util.mapper.BaseMapper

interface CartMapper : BaseMapper<Cart, RCart, RBCart, UCart> {
    override fun map(source: Cart): RCart {
        return RCart(
            id = source.id,
            sk = source.sk.toKey(),
            items = source.items.map {
                RCartItem(
                    quantity = it.quantity,
                    product = productService().rbMap(it.product)
                )
            }.toHashSet()
        )
    }

    override fun rbMap(source: Cart): RBCart {
        return RBCart(
            id = source.id,
            sk = source.sk.toKey()
        )
    }

    private fun productService(): ProductService = AppContextUtil.bean(ProductService::class.java)
}