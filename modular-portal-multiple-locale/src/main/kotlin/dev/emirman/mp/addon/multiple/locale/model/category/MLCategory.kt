package dev.emirman.mp.addon.multiple.locale.model.category

import dev.emirman.mp.core.model.category.Category
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import java.util.*

@Entity
class MLCategory() : Category("", "") {
    lateinit var locale: Locale

    @ManyToOne
    lateinit var category: Category


    constructor(locale: Locale, category: Category) : this() {
        this.locale = locale
        this.category = category
    }

    class Params : Category.Params() {
        var categoryId: String? = null
        var locale: String? = null
    }
}