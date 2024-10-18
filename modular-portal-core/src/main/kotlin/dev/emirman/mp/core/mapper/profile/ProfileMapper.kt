package dev.emirman.mp.core.mapper.profile

import dev.emirman.mp.core.dto.profile.read.RProfile
import dev.emirman.mp.core.dto.profile.update.UProfile
import dev.emirman.mp.core.model.user.User
import net.lubble.util.mapper.BaseMapper

interface ProfileMapper : BaseMapper<User, RProfile, RProfile, UProfile> {
    override fun map(source: UProfile, destination: User) {
        source.name?.let { destination.name = it }
        source.surname?.let { destination.surname = it }
        source.email?.let { destination.email = it }
    }

    override fun map(source: User): RProfile {
        return RProfile(
            id = source.id,
            name = source.name,
            surname = source.surname,
            email = source.email
        )
    }

    override fun rbMap(source: User): RProfile {
        throw UnsupportedOperationException()
    }
}