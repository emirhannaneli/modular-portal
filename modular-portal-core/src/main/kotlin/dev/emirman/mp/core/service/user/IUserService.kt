package dev.emirman.mp.core.service.user

import dev.emirman.mp.core.config.app.AppConfig
import dev.emirman.mp.core.dto.auth.SignUp
import dev.emirman.mp.core.dto.user.create.CUser
import dev.emirman.mp.core.dto.user.update.UUser
import dev.emirman.mp.core.event.user.UserEventPublisher
import dev.emirman.mp.core.exception.auth.EmailAlreadyExists
import dev.emirman.mp.core.exception.auth.RegistrationDisabled
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.repo.jpa.user.UserRepo
import dev.emirman.mp.core.spec.user.UserSpec
import net.lubble.util.exception.NotFoundException
import net.lubble.util.exception.WrongCredentials
import org.springframework.data.domain.Page
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class IUserService(
    val repo: UserRepo,
    val config: AppConfig.Security,
    val publisher: UserEventPublisher,
    val encoder: PasswordEncoder
) : UserService {
    override fun signUp(signUp: SignUp): User {
        if (!config.registrationEnabled) throw RegistrationDisabled()
        val spec = UserSpec()
        spec.params.email = signUp.email
        if (exists(spec)) throw EmailAlreadyExists(signUp.email)

        val user = User()
        user.email = signUp.email

        user.name = signUp.name
        user.surname = signUp.surname

        user.setPassword(signUp.password, encoder)

        val saved = save(user)

        publisher.publishNewUserEvent(saved)

        return saved
    }

    override fun create(create: CUser): User {
        val spec = UserSpec()
        spec.params.email = create.email()
        if (exists(spec)) throw EmailAlreadyExists(create.email())

        val user = User()
        user.email = create.email()

        user.name = create.name()
        user.surname = create.surname()

        user.setPassword(create.password, encoder)

        val saved = save(user)

        publisher.publishNewUserEvent(saved)

        return saved
    }

    override fun delete(base: User) {
        base.deleted = true
        repo.save(base)
    }

    override fun exists(spec: UserSpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: UserSpec): User {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: UserSpec): Page<User> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): User {
        val spec = UserSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: User): User {
        return repo.save(base)
    }

    override fun update(base: User, update: UUser) {
        map(update, base)
        save(base)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        try {
            val spec = UserSpec()
            spec.params.identifier = username
            return find(spec)
        } catch (e: NotFoundException) {
            throw WrongCredentials()
        }
    }
}