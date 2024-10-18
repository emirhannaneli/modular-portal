package dev.emirman.mp.core.model.warehouse

import jakarta.persistence.Entity
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel

@Entity
@Table(name = "warehouses")
open class Warehouse(
    open var title: String
) : BaseJPAModel() {
    open var address: String? = null
    open var phone: String? = null
    open var email: String? = null

    open class Params : SearchParams()
}