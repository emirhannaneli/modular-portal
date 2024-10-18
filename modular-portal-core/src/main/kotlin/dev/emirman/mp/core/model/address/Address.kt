package dev.emirman.mp.core.model.address

import dev.emirman.mp.core.model.user.User
import jakarta.persistence.*
import net.lubble.util.model.BaseJPAModel

@Entity
@Table(name = "addresses")
open class Address(
    open var title: String,
    open var name: String?,
    open var surname: String?,
    @Enumerated(EnumType.STRING)
    open var country: AddressCountry,
    open var city: String,
    open var district: String,
    open var address: String?,
    open var postal: String?,
    open var phone: String,
    @ManyToOne
    open var user: User
) : BaseJPAModel() {
    class Params : SearchParams() {
        var country: String? = null
        var city: String? = null
        var district: String? = null
        var postal: String? = null
        var phone: String? = null
        var userId: String? = null
    }
}