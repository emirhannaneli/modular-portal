package dev.emirman.mp.addon.multiple.locale.service.product

import dev.emirman.mp.addon.multiple.locale.dto.product.create.CMLProduct
import dev.emirman.mp.addon.multiple.locale.dto.product.update.UMLProduct
import dev.emirman.mp.addon.multiple.locale.model.product.MLProduct
import dev.emirman.mp.addon.multiple.locale.repo.product.MLProductRepo
import dev.emirman.mp.addon.multiple.locale.service.translator.TranslatorService
import dev.emirman.mp.addon.multiple.locale.spec.product.MLProductSpec
import dev.emirman.mp.core.service.content.ContentService
import dev.emirman.mp.core.service.product.ProductService
import net.lubble.util.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*

@Service
class IMLProductService(
    val repo: MLProductRepo,
    val service: ProductService,
    val translator: TranslatorService,
    val contentService: ContentService
) : MLProductService {
    override fun sync(id: String) {
        val product = service.findById(id)
        val spec = MLProductSpec()
        spec.params.productId = id

        val query = spec.ofSearch()
        val mlProducts = repo.findAll(query)

        for (mlProduct in mlProducts) {
            mlProduct.title = translator.translate(product.title, mlProduct.locale)
            mlProduct.description = product.description?.let { translator.translate(it, mlProduct.locale) }
            save(mlProduct)
        }
    }

    override fun generate(id: String, locales: HashSet<String>) {
        val product = service.findById(id)

        for (s in locales) {
            val locale = Locale.forLanguageTag(s)
            if (locale.language == "") continue
            val mlProduct = MLProduct(
                locale = locale,
                product = product
            )
            mlProduct.title = translator.translate(product.title, locale)
            mlProduct.description = product.description?.let { translator.translate(it, locale) }

            product.content?.let {
                val content = contentService.findById(it.toHexString())
                val translated = translator.translate(content.text!!, locale)
                content.text = translated
                contentService.save(content)
            }
            save(mlProduct)
        }
    }

    override fun create(create: CMLProduct): MLProduct {
        val product = service.create(create)

        lateinit var preview: MLProduct

        for (s in create.locales) {
            val locale = Locale.forLanguageTag(s)
            if (locale.language == "") continue
            val mlProduct = MLProduct(
                locale = locale,
                product = product
            )
            mlProduct.title = translator.translate(create.title, locale)
            mlProduct.description = create.description?.let { translator.translate(it, locale) }

            create.content?.let {
                val translated = translator.translate(it, locale)
                val content = contentService.create(translated)
                mlProduct.content = content
            }

            preview = save(mlProduct)
        }
        return preview
    }

    override fun delete(base: MLProduct) {
        service.delete(base)
    }

    override fun exists(spec: MLProductSpec): Boolean {
        val query = spec.ofSearch()
        return repo.exists(query)
    }

    override fun find(spec: MLProductSpec): MLProduct {
        val query = spec.ofSearch()
        return repo.findOne(query).orElseThrow { NotFoundException("params", spec.params) }
    }

    override fun findAll(spec: MLProductSpec): Page<MLProduct> {
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun findById(id: String): MLProduct {
        val spec = MLProductSpec()
        spec.params.id = id
        return find(spec)
    }

    override fun save(base: MLProduct): MLProduct {
        return repo.save(base)
    }

    override fun update(base: MLProduct, update: UMLProduct) {
        map(update, base)
        service.save(base)
    }

    override fun archive(base: MLProduct) {
        base.archived = true
        save(base)
    }

    override fun clearRecycleBin() {
        val spec = MLProductSpec()
        spec.params.deleted = true
        val query = spec.ofSearch()
        repo.delete(query)
    }

    override fun deletePermanently(id: String) {
        val spec = MLProductSpec()
        spec.params.id = id
        val query = spec.ofSearch()
        repo.delete(query)
    }

    override fun findAllArchived(spec: MLProductSpec): Page<MLProduct> {
        spec.params.archived = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun recycleBin(spec: MLProductSpec): Page<MLProduct> {
        spec.params.deleted = true
        val query = spec.ofSearch()
        val pageable = spec.ofSortedPageable()
        return repo.findAll(query, pageable)
    }

    override fun restore(id: String) {
        val spec = MLProductSpec()
        spec.params.id = id
        spec.params.deleted = true
        val mlProduct = find(spec)
        mlProduct.deleted = false
        save(mlProduct)
    }

    override fun unarchive(id: String) {
        val spec = MLProductSpec()
        spec.params.id = id
        spec.params.archived = true
        val mlProduct = find(spec)
        mlProduct.archived = false
        save(mlProduct)
    }
}