package dev.emirman.mp.core.spec.product

import dev.emirman.mp.core.mapper.category.CategoryMapper
import dev.emirman.mp.core.model.category.Category
import dev.emirman.mp.core.model.product.Product
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class ProductSpec(val params: Product.Params) : BaseSpec(params), BaseSpec.JPAModel<Product> {
    constructor() : this(Product.Params())

    override fun ofSearch(): Specification<Product> {
        return Specification { root, query, builder ->
            var predicate = defaultPredicates(root, query, builder, params)

            predicate = typePredicate(predicate, builder, root, Product::class.java)

            params.search?.let {
                val searchPredicate = searchPredicate(predicate, builder, root, it, "title", "description")
                predicate = builder.and(predicate, searchPredicate)
            }

            params.categoryId?.let {
                val join = root.join<Product, Category>("category", JoinType.LEFT)
                predicate = builder.and(predicate, idPredicate(builder, join, it))
                val childPredicate = childCategoryPredicate(predicate, builder, join, it)
                predicate = builder.or(predicate, childPredicate)
            }

            predicate
        }
    }

    private fun childCategoryPredicate(
        predicate: Predicate,
        builder: CriteriaBuilder,
        join: Join<Product, Category>,
        category: String
    ): Predicate {
        val childs = CategoryMapper.gatherChilds(category)

        for (child in childs) {
            val childPredicate = builder.or(predicate, idPredicate(builder, join, child.id.toString()))
            childCategoryPredicate(childPredicate, builder, join, child.id.toString())
        }
        return predicate
    }
}