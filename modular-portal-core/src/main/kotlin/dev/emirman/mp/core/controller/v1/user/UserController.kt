package dev.emirman.mp.core.controller.v1.user

import dev.emirman.mp.core.dto.user.create.CUser
import dev.emirman.mp.core.dto.user.read.RUser
import dev.emirman.mp.core.dto.user.update.UUser
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.user.UserService
import dev.emirman.mp.core.spec.user.UserSpec
import net.lubble.util.PageResponse
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("v1/users")
class UserController(val service: UserService) : BaseController<CUser, UUser, RUser, User.Params> {
    override fun create(create: CUser): ResponseEntity<RUser> {
        val user = service.create(create)
        val rUser = service.map(user)
        return ResponseEntity.created(URI("/v1/users/${rUser.sk}"))
            .body(rUser)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val user = service.findById(id)
        service.delete(user)

        return Response(
            message = "user.messages.delete.success",
        ).build()
    }

    override fun findAll(@ParameterObject params: User.Params): ResponseEntity<PageResponse> {
        val spec = UserSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val users = service.findAll(spec)
        val rUsers = service.map(users.content)

        return Response.ofPage(
            page = users,
            data = rUsers
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RUser> {
        val user = service.findById(id)
        val rUser = service.map(user)

        return ResponseEntity.ok(rUser)
    }

    override fun update(id: String, update: UUser): ResponseEntity<RUser> {
        val user = service.findById(id)
        service.update(user, update)
        val rUser = service.map(user)

        return ResponseEntity.ok(rUser)
    }
}