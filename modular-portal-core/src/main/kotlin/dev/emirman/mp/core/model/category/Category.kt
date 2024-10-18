package dev.emirman.mp.core.model.category

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.lubble.util.model.BaseJPAModel

@Entity
@Table(name = "categories")
open class Category(
    @Column(columnDefinition = "TEXT")
    open var title: String,
    @Column(columnDefinition = "TEXT")
    open var description: String,
) : BaseJPAModel() {
    @ManyToOne
    open var parent: Category? = null

    open class Params : SearchParams() {
        var parentId: String? = null
    }
}