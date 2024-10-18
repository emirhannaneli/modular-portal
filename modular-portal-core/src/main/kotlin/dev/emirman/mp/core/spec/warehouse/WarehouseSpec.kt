package dev.emirman.mp.core.spec.warehouse

import dev.emirman.mp.core.model.warehouse.Warehouse
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class WarehouseSpec(val params: Warehouse.Params) : BaseSpec(params), BaseSpec.JPAModel<Warehouse> {
    constructor() : this(Warehouse.Params())

    override fun ofSearch(): Specification<Warehouse> {
        return Specification { root, query, builder ->
            var predicates = defaultPredicates(root, query, builder, params)

            predicates
        }
    }
}