package dev.emirman.mp.core.dto.order.read

import dev.emirman.mp.core.dto.user.read.RBUser
import net.lubble.util.constant.EnumConstant
import net.lubble.util.dto.RBase
import java.math.BigDecimal

class RBOrder : RBase() {
    lateinit var user: RBUser
    lateinit var status: EnumConstant
    lateinit var amount: BigDecimal
    lateinit var type: EnumConstant
    var checkedOut: Boolean = false
}