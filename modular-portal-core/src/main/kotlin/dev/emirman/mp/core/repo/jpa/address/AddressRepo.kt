package dev.emirman.mp.core.repo.jpa.address

import dev.emirman.mp.core.model.address.Address
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface AddressRepo : BaseJPARepo<Address> {
}