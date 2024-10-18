package dev.emirman.mp.core.mapper.address

import dev.emirman.mp.core.dto.address.read.RAddress
import dev.emirman.mp.core.dto.address.read.RBAddress
import dev.emirman.mp.core.dto.address.update.UAddress
import dev.emirman.mp.core.model.address.Address
import net.lubble.util.mapper.BaseMapper

interface AddressMapper : BaseMapper<Address, RAddress, RBAddress, UAddress> {
    override fun map(source: Address): RAddress {
        return RAddress(
            title = source.title,
            name = source.name,
            surname = source.surname,
            country = source.country.toConstant(),
            city = source.city,
            district = source.district,
            address = source.address,
            postal = source.postal,
            phone = source.phone,
        ).apply {
            id = source.id
            sk = source.sk.toKey()
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: Address): RBAddress {
        return RBAddress(
            title = source.title,
            name = source.name,
            surname = source.surname,
            combined = "comibined string here"
        ).apply {
            id = source.id
            sk = source.sk.toKey()
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }
}