package dev.emirman.mp.core.repo.jpa.discount

import dev.emirman.mp.core.model.discount.Discount
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface DiscountRepo : BaseJPARepo<Discount> {
}