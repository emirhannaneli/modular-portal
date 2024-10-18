package dev.emirman.mp.core.dto.order.read

import dev.emirman.mp.core.dto.product.read.RBProduct
import dev.emirman.mp.core.dto.warehouse.read.RBWarehouse

class RBOrderItem {
    lateinit var id: String
    lateinit var sk: String
    lateinit var product: RBProduct
    lateinit var discount: String
    var warehouse: RBWarehouse? = null
}