package dev.emirman.mp.core.model.product

import dev.emirman.mp.core.model.category.Category
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel
import org.bson.types.ObjectId
import java.math.BigDecimal

@Entity
@Table(name = "products")
open class Product(
    @Column(columnDefinition = "TEXT")
    open var title: String,
    open var price: BigDecimal,
) : BaseJPAModel() {

    @Column(columnDefinition = "TEXT")
    open var description: String? = null

    open var content: ObjectId? = null

    @Column(columnDefinition = "TEXT")
    private var images: String? = null

    @ManyToOne
    open var category: Category? = null

    open fun images(): HashSet<String>? {
        return images?.split(",")?.toHashSet()
    }

    open fun images(images: HashSet<String>?) {
        this.images = images?.joinToString(",")
    }

    open class Params : SearchParams() {
        var categoryId: String? = null
    }
}