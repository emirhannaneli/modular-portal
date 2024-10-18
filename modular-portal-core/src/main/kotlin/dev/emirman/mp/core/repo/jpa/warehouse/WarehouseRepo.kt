package dev.emirman.mp.core.repo.jpa.warehouse

import dev.emirman.mp.core.model.warehouse.Warehouse
import net.lubble.util.repo.BaseJPARepo
import org.springframework.stereotype.Repository

@Repository
interface WarehouseRepo : BaseJPARepo<Warehouse> {
}