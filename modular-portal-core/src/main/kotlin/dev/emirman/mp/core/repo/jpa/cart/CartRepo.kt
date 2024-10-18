package dev.emirman.mp.core.repo.jpa.cart

import dev.emirman.mp.core.model.cart.Cart
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface CartRepo : BaseJPARepo<Cart> {
}