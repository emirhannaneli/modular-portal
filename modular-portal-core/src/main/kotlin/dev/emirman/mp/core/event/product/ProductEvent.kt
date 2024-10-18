package dev.emirman.mp.core.event.product

import dev.emirman.mp.core.model.product.Product
import org.springframework.context.ApplicationEvent

abstract class ProductEvent(product: Product) : ApplicationEvent(product)
class NewProductEvent(val product: Product) : ProductEvent(product)
class UpdateProductEvent(val product: Product) : ProductEvent(product)
class ArchiveProductEvent(val product: Product) : ProductEvent(product)
class UnarchiveProductEvent(val product: Product) : ProductEvent(product)
class DeleteProductEvent(val product: Product) : ProductEvent(product)
class RestoreProductEvent(val product: Product) : ProductEvent(product)
class DeletePermanentlyProductEvent(val product: Product) : ProductEvent(product)