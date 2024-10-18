package dev.emirman.mp.core.dto.cart.read

data class RCart(
    val id: Long,
    val sk: String,
    var items: HashSet<RCartItem>,
)