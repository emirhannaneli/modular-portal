package dev.emirman.mp.core.service.cart

import dev.emirman.mp.core.mapper.cart.CartMapper
import dev.emirman.mp.core.model.cart.Cart
import java.math.BigDecimal

interface CartService : CartMapper {
    fun save(cart: Cart): Cart

    fun cart(userId: String): Cart

    fun addProduct(userId: String, productId: String, quantity: BigDecimal = BigDecimal.ONE): Cart

    fun removeProduct(userId: String, productId: String): Cart

    fun updateProductQuantity(userId: String, productId: String, quantity: Int): Cart

    fun clearCart(userId: String): Cart
}