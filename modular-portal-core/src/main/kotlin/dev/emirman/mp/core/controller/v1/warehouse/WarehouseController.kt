package dev.emirman.mp.core.controller.v1.warehouse

import dev.emirman.mp.core.dto.warehouse.create.CWarehouse
import dev.emirman.mp.core.dto.warehouse.read.RWarehouse
import dev.emirman.mp.core.dto.warehouse.update.UWarehouse
import dev.emirman.mp.core.model.warehouse.Warehouse
import dev.emirman.mp.core.service.warehouse.WarehouseService
import dev.emirman.mp.core.spec.warehouse.WarehouseSpec
import net.lubble.util.PageResponse
import net.lubble.util.Response
import net.lubble.util.controller.BaseController
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("v1/warehouses")
class WarehouseController(val service: WarehouseService) : BaseController<CWarehouse, UWarehouse, RWarehouse, Warehouse.Params> {
    override fun create(create: CWarehouse): ResponseEntity<RWarehouse> {
        val warehouse = service.create(create)
        val uWarehouse = service.map(warehouse)
        return ResponseEntity
            .created(URI("/v1/warehouses/${warehouse.sk}"))
            .body(uWarehouse)
    }

    override fun delete(id: String): ResponseEntity<Response> {
        val warehouse = service.findById(id)
        service.delete(warehouse)
        return Response(
            message = "warehouse.messages.delete.success",
        ).build()
    }

    override fun findById(id: String): ResponseEntity<RWarehouse> {
        val warehouse = service.findById(id)
        val rWarehouse = service.map(warehouse)

        return ResponseEntity.ok(rWarehouse)
    }

    override fun update(id: String, update: UWarehouse): ResponseEntity<RWarehouse> {
        val warehouse = service.findById(id)
        service.update(warehouse, update)
        val rWarehouse = service.map(warehouse)

        return ResponseEntity.ok(rWarehouse)
    }

    override fun findAll(@ParameterObject params: Warehouse.Params): ResponseEntity<PageResponse> {
        val spec = WarehouseSpec(params)
        spec.params.deleted = false
        spec.params.archived = false

        val warehouses = service.findAll(spec)
        val rWarehouses = service.map(warehouses.content)

        return Response.ofPage(
            page = warehouses,
            data = rWarehouses
        ).build()
    }
}