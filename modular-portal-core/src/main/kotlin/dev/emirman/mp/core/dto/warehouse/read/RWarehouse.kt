package dev.emirman.mp.core.dto.warehouse.read

import net.lubble.util.dto.RBase

class RWarehouse : RBase() {
    lateinit var title: String
    var address: String? = null
    var phone: String? = null
    var email: String? = null
}