package dev.emirman.mp.core.service.cart

import dev.emirman.mp.core.model.cart.Cart
import dev.emirman.mp.core.model.cart.CartItem
import dev.emirman.mp.core.repo.jpa.cart.CartRepo
import dev.emirman.mp.core.repo.jpa.cart.ItemRepo
import dev.emirman.mp.core.service.product.ProductService
import dev.emirman.mp.core.spec.cart.CartSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ICartService(val repo: CartRepo, val itemRepo: ItemRepo, val service: ProductService) : CartService {
    override fun save(cart: Cart): Cart {
        return repo.save(cart)
    }

    override fun cart(userId: String): Cart {
        val spec = CartSpec()
        spec.params.userId = userId
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("user-id", userId) }
    }

    override fun addProduct(userId: String, productId: String, quantity: BigDecimal): Cart {
        val cart = cart(userId)

        val product = service.findById(productId)

        val exists = cart.items.any { it.product.matchesId(productId) }

        if (exists) {
            val item = cart.items.find { it.product.matchesId(productId) }!!
            item.quantity = item.quantity.plus(quantity)
        } else {
            val item = CartItem(
                cart = cart,
                product = product,
                quantity = quantity
            )
            cart.items.add(item)
        }

        return save(cart)
    }

    override fun removeProduct(userId: String, productId: String): Cart {
        val cart = cart(userId)

        cart.items.find { it.product.matchesId(productId) }?.let {
            cart.items.remove(it)
            itemRepo.delete(it)
        }

        return save(cart)
    }

    override fun updateProductQuantity(userId: String, productId: String, quantity: Int): Cart {
        val cart = cart(userId)

        val item = cart.items.find { it.product.matchesId(productId) } ?: throw NotFoundException("product-id", productId)

        item.quantity = BigDecimal(quantity).coerceAtLeast(BigDecimal.ONE)

        return save(cart)
    }

    override fun clearCart(userId: String): Cart {
        val cart = cart(userId)

        cart.items.clear()
        itemRepo.deleteAll(cart.items)

        return save(cart)
    }
}