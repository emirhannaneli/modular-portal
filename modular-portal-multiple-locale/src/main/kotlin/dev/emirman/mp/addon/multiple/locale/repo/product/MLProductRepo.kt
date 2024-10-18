package dev.emirman.mp.addon.multiple.locale.repo.product

import dev.emirman.mp.addon.multiple.locale.model.product.MLProduct
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface MLProductRepo : BaseJPARepo<MLProduct>