package dev.emirman.mp.core.dto.warehouse.read

import net.lubble.util.dto.RBase

class RBWarehouse : RBase() {
    lateinit var title: String
    var combined: String? = null
}