package dev.emirman.mp.core.service.address

import dev.emirman.mp.core.dto.address.create.CAddress
import dev.emirman.mp.core.dto.address.update.UAddress
import dev.emirman.mp.core.model.address.Address
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.repo.jpa.address.AddressRepo
import dev.emirman.mp.core.service.user.UserService
import dev.emirman.mp.core.spec.address.AddressSpec
import net.lubble.util.exception.NotFoundException
import net.lubble.util.helper.EnumHelper
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IAddressService(val repo: AddressRepo, val service: UserService) : AddressService {
    override fun save(address: Address): Address {
        return repo.save(address)
    }

    override fun address(spec: AddressSpec): Address {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { throw NotFoundException("params", spec.params) }
    }

    override fun addresses(spec: AddressSpec): Page<Address> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun addAddress(user: User, create: CAddress): Address {
        val address = Address(
            title = create.title,
            name = create.name,
            surname = create.surname,
            country = EnumHelper.findByValue(create.country),
            city = create.city,
            district = create.district,
            address = create.address,
            postal = create.postal,
            phone = create.phone,
            user = user
        )
        return save(address)
    }

    override fun updateAddress(address: Address, update: UAddress): Address {
        map(update, address)
        return save(address)
    }

    override fun deleteAddress(address: Address) {
        repo.delete(address)
    }
}