package dev.emirman.mp.core.repo.mongo.order

import dev.emirman.mp.core.model.order.Order
import net.lubble.util.repo.BaseMongoRepo
import org.springframework.stereotype.Repository

@Repository
interface OrderRepo : BaseMongoRepo<Order> {
}