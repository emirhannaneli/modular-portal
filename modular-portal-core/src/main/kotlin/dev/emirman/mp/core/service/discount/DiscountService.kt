package dev.emirman.mp.core.service.discount

import dev.emirman.mp.core.dto.discount.create.CDiscount
import dev.emirman.mp.core.dto.discount.update.UDiscount
import dev.emirman.mp.core.mapper.discount.DiscountMapper
import dev.emirman.mp.core.model.discount.Discount
import dev.emirman.mp.core.spec.discount.DiscountSpec
import net.lubble.util.service.BaseService

interface DiscountService : BaseService<Discount, CDiscount, UDiscount, DiscountSpec>, DiscountMapper {
    fun count(spec: DiscountSpec)
}