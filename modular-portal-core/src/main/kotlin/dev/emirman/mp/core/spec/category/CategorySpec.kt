package dev.emirman.mp.core.spec.category

import dev.emirman.mp.core.model.category.Category
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification

class CategorySpec(val params: Category.Params) : BaseSpec(params), BaseSpec.JPAModel<Category> {
    constructor() : this(Category.Params())

    override fun ofSearch(): Specification<Category> {
        return Specification { root, query, builder ->
            var predicates = defaultPredicates(root, query, builder, params)

            predicates = typePredicate(predicates, builder, root, Category::class.java)

            params.search?.let {
                val searchPredicate = searchPredicate(predicates, builder, root, it, "title", "description")
                predicates = builder.and(predicates, searchPredicate)
            }

            params.parentId?.let {
                val join = root.join<Category, Category>("parent")
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            predicates
        }
    }
}