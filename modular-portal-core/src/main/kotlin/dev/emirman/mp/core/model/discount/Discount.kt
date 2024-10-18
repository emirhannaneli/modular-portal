package dev.emirman.mp.core.model.discount

import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.model.user.User
import jakarta.persistence.CollectionTable
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "discounts")
open class Discount(
    open var amount: BigDecimal
) : BaseJPAModel() {
    @ManyToMany(targetEntity = Product::class)
    @CollectionTable(name = "discounts_products")
    open var products: Set<Product> = hashSetOf()

    @ManyToMany(targetEntity = User::class)
    @CollectionTable(name = "discounts_users")
    open var users: Set<User> = hashSetOf()

    open var allProducts: Boolean = false

    open var allUsers: Boolean = false

    open var startDate: Date? = null

    open var endDate: Date? = null

    open var always: Boolean = false

    open var active: Boolean = false

    open class Params : SearchParams() {
        var productId: String? = null
        var userId: String? = null
        var allProducts: Boolean? = null
        var allUsers: Boolean? = null
        var startDate: String? = null
        var endDate: String? = null
        var always: Boolean? = null
        var active: Boolean? = null
    }
}