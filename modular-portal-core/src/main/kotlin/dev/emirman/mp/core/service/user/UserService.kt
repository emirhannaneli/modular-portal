package dev.emirman.mp.core.service.user

import dev.emirman.mp.core.dto.auth.SignUp
import dev.emirman.mp.core.dto.user.create.CUser
import dev.emirman.mp.core.dto.user.update.UUser
import dev.emirman.mp.core.mapper.user.UserMapper
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.spec.user.UserSpec
import net.lubble.util.service.BaseService
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : BaseService<User, CUser, UUser, UserSpec>, UserMapper, UserDetailsService {
    fun signUp(signUp: SignUp): User
}