package dev.emirman.mp.addon.multiple.locale.dto.product.read

import dev.emirman.mp.core.dto.product.read.RProduct

class RMLProduct : RProduct() {
    lateinit var locale: String
}