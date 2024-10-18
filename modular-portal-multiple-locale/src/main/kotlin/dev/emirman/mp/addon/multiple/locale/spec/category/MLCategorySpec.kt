package dev.emirman.mp.addon.multiple.locale.spec.category

import dev.emirman.mp.addon.multiple.locale.model.category.MLCategory
import dev.emirman.mp.core.model.category.Category
import jakarta.persistence.criteria.JoinType
import net.lubble.util.spec.BaseSpec
import org.springframework.data.jpa.domain.Specification
import java.util.*

class MLCategorySpec(val params: MLCategory.Params) : BaseSpec(params), BaseSpec.JPAModel<MLCategory> {
    constructor() : this(MLCategory.Params())

    override fun ofSearch(): Specification<MLCategory> {
        return Specification { root, _, builder ->
            var predicates = defaultPredicates(root, builder, params)
            params.locale?.let {
                predicates = builder.and(predicates, builder.equal(root.get<Locale>("locale"), Locale.forLanguageTag(it)))
            }

            params.categoryId?.let {
                val join = root.join<MLCategory, Category>("category", JoinType.LEFT)
                predicates = builder.and(predicates, idPredicate(builder, join, it))
            }

            predicates
        }
    }
}