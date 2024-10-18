package dev.emirman.mp.core.dto.category.create

import jakarta.validation.constraints.NotBlank
import net.lubble.util.ReFormat


open class CCategory {
    @NotBlank
    lateinit var title: String

    @NotBlank
    lateinit var description: String

    var childs: HashSet<String>? = null

    fun format() {
        title = ReFormat(title).trim().format()
        description = ReFormat(description).trim().format()
    }
}