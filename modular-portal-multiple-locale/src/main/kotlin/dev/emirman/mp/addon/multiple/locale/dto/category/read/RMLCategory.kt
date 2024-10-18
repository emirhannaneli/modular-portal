package dev.emirman.mp.addon.multiple.locale.dto.category.read

import dev.emirman.mp.core.dto.category.read.RCategory

class RMLCategory : RCategory() {
    lateinit var locale: String
}