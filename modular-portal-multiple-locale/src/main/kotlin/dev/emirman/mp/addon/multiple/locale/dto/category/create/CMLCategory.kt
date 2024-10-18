package dev.emirman.mp.addon.multiple.locale.dto.category.create

import dev.emirman.mp.core.dto.category.create.CCategory
import jakarta.validation.constraints.NotEmpty

class CMLCategory : CCategory() {
    @NotEmpty
    lateinit var locales: HashSet<String>
}