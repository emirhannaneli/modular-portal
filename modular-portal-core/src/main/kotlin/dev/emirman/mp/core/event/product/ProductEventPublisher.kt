package dev.emirman.mp.core.event.product

import dev.emirman.mp.core.model.product.Product
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProductEventPublisher(val publisher: ApplicationEventPublisher) {
    fun publishNewProductEvent(product: Product) {
        publisher.publishEvent(NewProductEvent(product))
    }

    fun publishUpdateProductEvent(product: Product) {
        publisher.publishEvent(UpdateProductEvent(product))
    }

    fun publishArchiveProductEvent(product: Product) {
        publisher.publishEvent(ArchiveProductEvent(product))
    }

    fun publishUnarchiveProductEvent(product: Product) {
        publisher.publishEvent(UnarchiveProductEvent(product))
    }

    fun publishDeleteProductEvent(product: Product) {
        publisher.publishEvent(DeleteProductEvent(product))
    }

    fun publishRestoreProductEvent(product: Product) {
        publisher.publishEvent(RestoreProductEvent(product))
    }

    fun publishDeletePermanentlyProductEvent(product: Product) {
        publisher.publishEvent(DeletePermanentlyProductEvent(product))
    }
}