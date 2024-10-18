package dev.emirman.mp.core.service.product

import dev.emirman.mp.core.dto.product.create.CProduct
import dev.emirman.mp.core.dto.product.update.UProduct
import dev.emirman.mp.core.event.product.ProductEventPublisher
import dev.emirman.mp.core.model.product.Product
import dev.emirman.mp.core.repo.jpa.product.ProductRepo
import dev.emirman.mp.core.service.content.ContentService
import dev.emirman.mp.core.spec.product.ProductSpec
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class IProductService(val repo: ProductRepo, val contentService: ContentService, val publisher: ProductEventPublisher) : ProductService {
    override fun create(create: CProduct): Product {
        val product = Product(
            title = create.title,
            price = create.price,
        )

        create.content?.let {
            val content = contentService.create(it)
            product.content = content
        }

        product.description = create.description
        product.images(create.images)

        val saved = save(product)

        publisher.publishNewProductEvent(saved)

        return saved
    }

    override fun delete(base: Product) {
        base.deleted = true
        save(base)
        publisher.publishDeleteProductEvent(base)
    }

    override fun exists(spec: ProductSpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: ProductSpec): Product {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { throw NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: ProductSpec): Page<Product> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): Product {
        val spec = ProductSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: Product): Product {
        return repo.save(base)
    }

    override fun update(base: Product, update: UProduct) {
        map(update, base)
        save(base)
        publisher.publishUpdateProductEvent(base)
    }

    override fun archive(base: Product) {
        base.archived = true
        save(base)
        publisher.publishArchiveProductEvent(base)
    }

    override fun clearRecycleBin() {
        val spec = ProductSpec()
        spec.params.deleted = true
        val query = spec.ofSearch()
        repo.delete(query)
    }

    override fun deletePermanently(id: String) {
        val spec = ProductSpec()
        spec.params.id = id
        spec.params.deleted = true
        val product = find(spec)
        publisher.publishDeletePermanentlyProductEvent(product)
        repo.delete(product)
    }

    override fun findAllArchived(spec: ProductSpec): Page<Product> {
        spec.params.archived = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun recycleBin(spec: ProductSpec): Page<Product> {
        spec.params.deleted = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun restore(id: String) {
        val spec = ProductSpec()
        spec.params.id = id
        spec.params.deleted = true
        val product = find(spec)
        product.deleted = false
        save(product)
        publisher.publishRestoreProductEvent(product)
    }

    override fun unarchive(id: String) {
        val spec = ProductSpec()
        spec.params.id = id
        spec.params.archived = true
        val product = find(spec)
        product.archived = false
        save(product)
        publisher.publishUnarchiveProductEvent(product)
    }
}