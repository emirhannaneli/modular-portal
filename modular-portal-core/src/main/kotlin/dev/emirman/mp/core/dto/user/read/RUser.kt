package dev.emirman.mp.core.dto.user.read

import net.lubble.util.dto.RBase

class RUser : RBase() {
    var name: String? = null
    var surname: String? = null
    lateinit var email: String
}