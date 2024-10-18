package dev.emirman.mp.core.model.cart

import dev.emirman.mp.core.model.user.User
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel

@Entity
@Table(name = "carts")
open class Cart : BaseJPAModel() {

    @OneToMany(mappedBy = "cart", cascade = [ALL])
    open var items: MutableSet<CartItem> = mutableSetOf()

    @OneToOne(mappedBy = "cart", cascade = [ALL])
    open var user: User? = null

    open class Params : SearchParams() {
        var userId: String? = null
    }
}