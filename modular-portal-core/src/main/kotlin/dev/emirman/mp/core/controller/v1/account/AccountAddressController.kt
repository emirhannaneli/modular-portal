package dev.emirman.mp.core.controller.v1.account

import dev.emirman.mp.core.dto.address.create.CAddress
import dev.emirman.mp.core.dto.address.update.UAddress
import dev.emirman.mp.core.model.address.Address
import dev.emirman.mp.core.model.user.User
import dev.emirman.mp.core.service.address.AddressService
import dev.emirman.mp.core.spec.address.AddressSpec
import net.lubble.util.AuthTool
import net.lubble.util.PageResponse
import net.lubble.util.Response
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/account/addresses")
class AccountAddressController(val service: AddressService) {
    @GetMapping
    fun addresses(@ParameterObject params: Address.Params): ResponseEntity<PageResponse> {
        val current = AuthTool.principal<User>()!!
        params.userId = current.id.toString()
        val spec = AddressSpec(params)

        val addresses = service.addresses(spec)
        val rAddresses = service.map(addresses.content)

        return Response.ofPage(
            page = addresses,
            data = rAddresses
        ).build()
    }

    @PostMapping
    fun addAddress(@RequestBody create: CAddress): ResponseEntity<Response> {
        create.format()

        val current = AuthTool.principal<User>()!!
        service.addAddress(current, create)

        return Response(
            message = "address.messages.create.success"
        ).build()
    }

    @PutMapping("{id}")
    fun updateAddress(@PathVariable id: String, @RequestBody update: UAddress): ResponseEntity<Response> {
        val current = AuthTool.principal<User>()!!
        val spec = AddressSpec()
        spec.params.id = id
        spec.params.userId = current.id.toString()

        val address = service.address(spec)
        service.updateAddress(address, update)

        return Response(
            message = "address.messages.update.success"
        ).build()
    }

    @DeleteMapping
    fun deleteAddress(): ResponseEntity<Response> {
        val current = AuthTool.principal<User>()!!
        val spec = AddressSpec()
        spec.params.userId = current.id.toString()

        val address = service.address(spec)
        service.deleteAddress(address)

        return Response(
            message = "address.messages.delete.success"
        ).build()
    }
}