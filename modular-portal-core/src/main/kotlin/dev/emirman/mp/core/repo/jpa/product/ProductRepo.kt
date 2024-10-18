package dev.emirman.mp.core.repo.jpa.product

import dev.emirman.mp.core.model.product.Product
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface ProductRepo : BaseJPARepo<Product> {
}