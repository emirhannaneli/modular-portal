package dev.emirman.mp.core.repo.jpa.category

import dev.emirman.mp.core.model.category.Category
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepo : BaseJPARepo<Category> {
}