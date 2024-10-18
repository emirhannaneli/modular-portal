package dev.emirman.mp.core.dto.category.read

import net.lubble.util.dto.RBase

open class RBCategory : RBase() {
    var title: String? = null
    var haveChilds: Boolean = false
}