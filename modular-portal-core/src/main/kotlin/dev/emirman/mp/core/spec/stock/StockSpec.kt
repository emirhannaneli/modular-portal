package dev.emirman.mp.core.spec.stock

import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.model.stock.Stock
import dev.emirman.mp.core.model.warehouse.Warehouse
import jakarta.persistence.criteria.JoinType
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification


class StockSpec(val params: Stock.Params) : BaseSpec(params), BaseSpec.JPAModel<Stock> {
    constructor() : this(Stock.Params())

    override fun ofSearch(): Specification<Stock> {
        return Specification { root, query, builder ->
            var predicates = defaultPredicates(root, query, builder, params)

            params.productId?.let {
                val join = root.join<Stock, Product>("product", JoinType.LEFT)
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            params.warehouseId?.let {
                val join = root.join<Stock, Warehouse>("warehouse", JoinType.LEFT)
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            params.userId?.let {
                val join = root.join<Stock, Warehouse>("user", JoinType.LEFT)
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            predicates
        }
    }
}