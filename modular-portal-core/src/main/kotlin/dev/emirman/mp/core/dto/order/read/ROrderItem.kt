package dev.emirman.mp.core.dto.order.read

import dev.emirman.mp.core.dto.product.read.RProduct
import dev.emirman.mp.core.dto.warehouse.read.RWarehouse

class ROrderItem {
    lateinit var id: String
    lateinit var sk: String
    lateinit var product: RProduct
    lateinit var discount: String
    var warehouse: RWarehouse? = null
}