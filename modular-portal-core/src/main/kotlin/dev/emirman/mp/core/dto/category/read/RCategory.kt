package dev.emirman.mp.core.dto.category.read

import net.lubble.util.dto.RBase

open class RCategory : RBase() {
    var title: String? = null
    var description: String? = null
    var childs: List<RBCategory>? = null
}