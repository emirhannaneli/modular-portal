package dev.emirman.mp.core.spec.address

import dev.emirman.mp.core.model.address.Address
import net.lubble.util.spec.BaseSpec
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.data.jpa.domain.Specification

class AddressSpec(val params: Address.Params) : BaseSpec(params), BaseSpec.JPAModel<Address> {
    constructor() : this(Address.Params())

    override fun ofSearch(): Specification<Address> {
        return Specification { root, query, builder ->
            var predicate = defaultPredicates(root, query, builder, params)

            params.search?.let {
                val searchPredicate = searchPredicate(
                    predicate,
                    builder,
                    root,
                    it,
                    "title",
                    "name",
                    "surname",
                    "city",
                    "district",
                    "address",
                    "postal",
                    "phone"
                )
                predicate = builder.and(predicate, searchPredicate)
            }

            params.country?.let {
                predicate = builder.and(predicate, builder.equal(root.get<String>("country"), it))
            }

            params.city?.let {
                predicate = builder.and(predicate, builder.equal(root.get<String>("city"), it))
            }

            params.district?.let {
                predicate = builder.and(predicate, builder.equal(root.get<String>("district"), it))
            }

            params.postal?.let {
                predicate = builder.and(predicate, builder.equal(root.get<String>("postal"), it))
            }

            params.phone?.let {
                predicate = builder.and(predicate, builder.equal(root.get<String>("phone"), it))
            }

            params.userId?.let {
                val join = root.join<Address, User>("user")
                predicate = builder.and(predicate, idPredicate(builder, join, it))
            }

            predicate
        }
    }
}