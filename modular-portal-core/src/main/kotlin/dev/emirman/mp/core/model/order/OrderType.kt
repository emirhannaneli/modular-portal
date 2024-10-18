package dev.emirman.mp.core.model.order

import net.lubble.util.helper.EnumHelper

enum class OrderType : EnumHelper {
    SELLING {
        override val color: String?
            get() = null
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.type.selling"
        override val value: String
            get() = name
    },
    BUYING {
        override val color: String?
            get() = null
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.type.buying"
        override val value: String
            get() = name
    };
}