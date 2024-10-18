package dev.emirman.mp.addon.multiple.locale.repo.category

import dev.emirman.mp.addon.multiple.locale.model.category.MLCategory
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface MLCategoryRepo : BaseJPARepo<MLCategory>