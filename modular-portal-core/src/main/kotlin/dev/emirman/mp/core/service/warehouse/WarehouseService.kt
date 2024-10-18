package dev.emirman.mp.core.service.warehouse

import dev.emirman.mp.core.dto.warehouse.create.CWarehouse
import dev.emirman.mp.core.dto.warehouse.update.UWarehouse
import dev.emirman.mp.core.mapper.warehouse.WarehouseMapper
import dev.emirman.mp.core.model.warehouse.Warehouse
import dev.emirman.mp.core.spec.warehouse.WarehouseSpec
import net.lubble.util.service.BaseService

interface WarehouseService : BaseService<Warehouse, CWarehouse, UWarehouse, WarehouseSpec>, WarehouseMapper {
}