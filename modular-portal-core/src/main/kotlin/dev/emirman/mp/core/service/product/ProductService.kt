package dev.emirman.mp.core.service.product

import dev.emirman.mp.core.dto.product.create.CProduct
import dev.emirman.mp.core.dto.product.update.UProduct
import dev.emirman.mp.core.mapper.product.ProductMapper
import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.spec.product.ProductSpec
import net.lubble.util.service.BaseService

interface ProductService : BaseService<Product, CProduct, UProduct, ProductSpec>, ProductMapper {
}