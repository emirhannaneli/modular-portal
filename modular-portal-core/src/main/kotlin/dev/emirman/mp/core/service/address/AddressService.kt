package dev.emirman.mp.core.service.address

import dev.emirman.mp.core.dto.address.create.CAddress
import dev.emirman.mp.core.dto.address.update.UAddress
import dev.emirman.mp.core.mapper.address.AddressMapper
import dev.emirman.mp.core.model.address.Address
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.spec.address.AddressSpec
import org.springframework.data.domain.Page

interface AddressService : AddressMapper {
    fun save(address: Address): Address

    fun address(spec: AddressSpec): Address

    fun addresses(spec: AddressSpec): Page<Address>

    fun addAddress(user: User, create: CAddress): Address

    fun updateAddress(address: Address, update: UAddress): Address

    fun deleteAddress(address: Address)
}