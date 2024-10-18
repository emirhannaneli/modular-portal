package dev.emirman.mp.addon.multiple.locale.listener

import dev.emirman.mp.addon.multiple.locale.service.product.MLProductService
import dev.emirman.mp.addon.multiple.locale.spec.product.MLProductSpec
import dev.emirman.mp.core.event.product.*
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ProductListener(val service: MLProductService) {
    @EventListener(ArchiveProductEvent::class)
    fun onArchiveProduct(event: ArchiveProductEvent) {
        val spec = MLProductSpec()
        spec.params.productId = event.product.id.toString()
        val mlProducts = service.findAll(spec)
        mlProducts.forEach {
            it.archived = true
            service.save(it)
        }
    }

    @EventListener(UnarchiveProductEvent::class)
    fun onUnarchiveProduct(event: UnarchiveProductEvent) {
        val spec = MLProductSpec()
        spec.params.productId = event.product.id.toString()
        val mlProducts = service.findAll(spec)
        mlProducts.forEach {
            it.archived = false
            service.save(it)
        }
    }

    @EventListener(DeleteProductEvent::class)
    fun onDeleteProduct(event: DeleteProductEvent) {
        val spec = MLProductSpec()
        spec.params.productId = event.product.id.toString()
        val mlProducts = service.findAll(spec)
        mlProducts.forEach {
            service.delete(it)
        }
    }

    @EventListener(RestoreProductEvent::class)
    fun onRestoreProduct(event: RestoreProductEvent) {
        val spec = MLProductSpec()
        spec.params.productId = event.product.id.toString()
        val mlProducts = service.findAll(spec)
        mlProducts.forEach {
            it.deleted = false
            service.save(it)
        }
    }

    @EventListener(DeletePermanentlyProductEvent::class)
    fun onDeletePermanentlyProduct(event: DeletePermanentlyProductEvent) {
        val spec = MLProductSpec()
        spec.params.productId = event.product.id.toString()
        val mlProducts = service.findAll(spec)
        mlProducts.forEach {
            service.deletePermanently(it.id.toString())
        }
    }


}