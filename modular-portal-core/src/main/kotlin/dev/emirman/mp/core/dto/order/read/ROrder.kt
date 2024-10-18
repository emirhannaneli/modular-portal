package dev.emirman.mp.core.dto.order.read

import dev.emirman.mp.core.dto.user.read.RUser
import net.lubble.util.constant.EnumConstant
import net.lubble.util.dto.RBase
import java.math.BigDecimal

class ROrder : RBase() {
    lateinit var user: RUser
    lateinit var status: EnumConstant
    lateinit var amount: BigDecimal
    lateinit var type: EnumConstant
    var checkedOut: Boolean = false
}