package dev.emirman.mp.addon.multiple.locale.dto.product.create

import dev.emirman.mp.core.dto.product.create.CProduct
import jakarta.validation.constraints.NotEmpty

class CMLProduct : CProduct() {
    @NotEmpty
    lateinit var locales: HashSet<String>
}