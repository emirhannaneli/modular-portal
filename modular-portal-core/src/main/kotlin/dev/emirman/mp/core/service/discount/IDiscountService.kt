package dev.emirman.mp.core.service.discount

import dev.emirman.mp.core.dto.discount.create.CDiscount
import dev.emirman.mp.core.dto.discount.update.UDiscount
import dev.emirman.mp.core.model.discount.Discount
import dev.emirman.mp.core.repo.jpa.discount.DiscountRepo
import dev.emirman.mp.core.service.product.ProductService
import dev.emirman.mp.core.service.user.UserService
import dev.emirman.mp.core.spec.discount.DiscountSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IDiscountService(val repo: DiscountRepo, val productService: ProductService, val userService: UserService) : DiscountService {
    override fun count(spec: DiscountSpec) {
        TODO("Not yet implemented")
    }

    override fun create(create: CDiscount): Discount {
        val discount = Discount(create.amount)

        create.products?.let { id ->
            discount.products = id.map { productService.findById(it) }.toMutableSet()
        }

        create.users?.let { id ->
            discount.users = id.map { userService.findById(it) }.toMutableSet()
        }

        create.allProducts?.let { discount.allProducts = it }
        create.allUsers?.let { discount.allUsers = it }
        create.startDate?.let { discount.startDate = it }
        create.endDate?.let { discount.endDate = it }
        create.always?.let { discount.always = it }
        create.active?.let { discount.active = it }

        return save(discount)
    }

    override fun delete(base: Discount) {
        base.deleted = true
        repo.save(base)
    }

    override fun exists(spec: DiscountSpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: DiscountSpec): Discount {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("Discount not found", spec.params) }
    }

    override fun findAll(spec: DiscountSpec): Page<Discount> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): Discount {
        val spec = DiscountSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: Discount): Discount {
        return repo.save(base)
    }

    override fun update(base: Discount, update: UDiscount) {
        map(update, base)
        save(base)
    }
}