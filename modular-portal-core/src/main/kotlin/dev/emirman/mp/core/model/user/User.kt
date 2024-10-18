package dev.emirman.mp.core.model.user

import dev.emirman.mp.core.model.cart.Cart
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Transient
import net.lubble.util.ReFormat
import net.lubble.util.model.BaseJPAModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@Entity
@Table(name = "users")
open class User : BaseJPAModel(), UserDetails {
    open lateinit var email: String

    private lateinit var password: String

    open var name: String? = null

    open var surname: String? = null

    open var locale: Locale? = null

    open var lastLogin: Date = Date()

    @OneToOne(cascade = [ALL])
    open var cart: Cart = Cart()

    open class Params : SearchParams() {
        var name: String? = null
            get() = field?.let { ReFormat(it).trim().lower().format() }

        var surname: String? = null
            get() = field?.let { ReFormat(it).trim().lower().format() }

        var email: String? = null
            get() = field?.let { ReFormat(it).trim().lower().format() }

        var identifier: String? = null
            get() = field?.let { ReFormat(it).trim().lower().format() }
    }

    @Transient
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    @Transient
    override fun getPassword(): String {
        return password
    }

    open fun matchPassword(password: String, encoder: PasswordEncoder): Boolean {
        return encoder.matches(password, this.password)
    }

    open fun setPassword(password: String, encoder: PasswordEncoder) {
        this.password = encoder.encode(password)
    }

    @Transient
    override fun getUsername(): String {
        return email
    }

    @Transient
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @Transient
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @Transient
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @Transient
    override fun isEnabled(): Boolean {
        return true
    }
}