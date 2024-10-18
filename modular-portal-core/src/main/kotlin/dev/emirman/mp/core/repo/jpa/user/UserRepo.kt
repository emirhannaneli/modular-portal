package dev.emirman.mp.core.repo.jpa.user

import dev.emirman.mp.core.model.user.User
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : BaseJPARepo<User> {
}