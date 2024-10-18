package dev.emirman.mp.addon.stock.tracking.model

import java.math.BigDecimal

class Stock {
    lateinit var id: String
    lateinit var min: MinMax
    lateinit var max: MinMax

    class MinMax {
        var value: BigDecimal = BigDecimal.ZERO
        lateinit var notification: Notification
    }

    class Notification {
        var enabled: Boolean = false
        lateinit var template: String
        lateinit var types: List<Type>
        lateinit var remind: Remind

        class Remind {
            var enabled: Boolean = false
            var interval: Int = 0
        }

        enum class Type {
            SMS, PUSH, MAIL
        }
    }
}