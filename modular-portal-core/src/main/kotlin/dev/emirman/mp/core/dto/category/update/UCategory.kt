package dev.emirman.mp.core.dto.category.update

open class UCategory {
    var title: String? = null
    var description: String? = null
    var childs: HashSet<String>? = null
}