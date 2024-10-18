package dev.emirman.mp.core.model.order

import net.lubble.util.helper.EnumHelper

enum class OrderStatus : EnumHelper {
    PENDING {
        override val color: String
            get() = "#FFC800"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.pending"
        override val value: String
            get() = name
    },
    ACCEPTED {
        override val color: String
            get() = "#00FF00"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.accepted"
        override val value: String
            get() = name
    },
    REJECTED {
        override val color: String
            get() = "#FF0000"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.rejected"
        override val value: String
            get() = name
    },
    CANCELED {
        override val color: String
            get() = "#FF0000"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.canceled"
        override val value: String
            get() = name
    },
    DELIVERED {
        override val color: String
            get() = "#00A2FF"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.delivered"
        override val value: String
            get() = name
    },
    COMPLETED {
        override val color: String
            get() = "#00A2FF"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.completed"
        override val value: String
            get() = name
    },
    REFUNDED {
        override val color: String
            get() = "#D67A45"
        override val icon: String?
            get() = null
        override val label: String
            get() = "order.status.refunded"
        override val value: String
            get() = name
    };
}