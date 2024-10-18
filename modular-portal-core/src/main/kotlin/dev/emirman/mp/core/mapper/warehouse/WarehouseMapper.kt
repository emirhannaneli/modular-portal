package dev.emirman.mp.core.mapper.warehouse

import dev.emirman.mp.core.dto.warehouse.read.RBWarehouse
import dev.emirman.mp.core.dto.warehouse.read.RWarehouse
import dev.emirman.mp.core.dto.warehouse.update.UWarehouse
import dev.emirman.mp.core.model.warehouse.Warehouse
import net.lubble.util.mapper.BaseMapper

interface WarehouseMapper : BaseMapper<Warehouse, RWarehouse, RBWarehouse, UWarehouse> {
    override fun map(source: Warehouse): RWarehouse {
        return RWarehouse().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            address = source.address
            phone = source.phone
            email = source.email
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }

    override fun rbMap(source: Warehouse): RBWarehouse {
        return RBWarehouse().apply {
            id = source.id
            sk = source.sk.toKey()
            title = source.title
            combined = "${source.address} (${source.phone}) - ${source.email}"
            createdAt = source.createdAt
            updatedAt = source.updatedAt
        }
    }
}