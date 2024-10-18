package dev.emirman.mp.core.service.warehouse

import dev.emirman.mp.core.dto.warehouse.create.CWarehouse
import dev.emirman.mp.core.dto.warehouse.update.UWarehouse
import dev.emirman.mp.core.model.warehouse.Warehouse
import dev.emirman.mp.core.repo.jpa.warehouse.WarehouseRepo
import dev.emirman.mp.core.spec.warehouse.WarehouseSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IWarehouseService(val repo: WarehouseRepo) : WarehouseService {
    override fun create(create: CWarehouse): Warehouse {
        val warehouse = Warehouse(
            title = create.title,
        )

        warehouse.address = create.address
        warehouse.phone = create.phone
        warehouse.email = create.email

        return save(warehouse)
    }

    override fun delete(base: Warehouse) {
        base.deleted = true
        save(base)
    }

    override fun exists(spec: WarehouseSpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: WarehouseSpec): Warehouse {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: WarehouseSpec): Page<Warehouse> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): Warehouse {
        val spec = WarehouseSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: Warehouse): Warehouse {
        return repo.save(base)
    }

    override fun update(base: Warehouse, update: UWarehouse) {
        map(update, base)
        save(base)
    }
}