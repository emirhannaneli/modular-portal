package dev.emirman.mp.core.mapper.user

import dev.emirman.mp.core.dto.user.read.RBUser
import dev.emirman.mp.core.dto.user.read.RUser
import dev.emirman.mp.core.dto.user.update.UUser
import dev.emirman.mp.core.model.user.User
import net.lubble.util.mapper.BaseMapper

interface UserMapper : BaseMapper<User, RUser, RBUser, UUser> {
    override fun map(source: UUser, destination: User) {
        source.name?.let { destination.name = it }
        source.surname?.let { destination.surname = it }
        source.email?.let { destination.email = it }
    }

    override fun map(source: User): RUser {
        return RUser().apply {
            id = source.id
            sk = source.sk.toKey()
            name = source.name
            surname = source.surname
            email = source.email
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: User): RBUser {
        return RBUser().apply {
            id = source.id
            sk = source.sk.toKey()
            email = source.email
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }
}