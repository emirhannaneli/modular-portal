package dev.emirman.mp.core.model.verification

import dev.emirman.mp.core.model.user.User
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.lubble.util.Generator
import net.lubble.util.model.BaseJPAModel
import org.joda.time.DateTime
import java.util.*

@Entity
@Table(name = "verifications")
open class Verification(
    @ManyToOne
    open val user: User,
    private val expireAt: Date = DateTime.now().plusHours(1).toDate(),
    private val code: String = Generator.code()
) : BaseJPAModel() {

    fun verify(code: String): Boolean {
        return this.code == code && expireAt.after(DateTime.now().toDate())
    }

    open class Params : SearchParams() {
        var userId: Long? = null
    }
}