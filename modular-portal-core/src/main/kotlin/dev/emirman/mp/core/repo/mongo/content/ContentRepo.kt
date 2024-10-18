package dev.emirman.mp.core.repo.mongo.content

import dev.emirman.mp.core.model.content.Content
import net.lubble.util.repo.BaseMongoRepo
import org.springframework.stereotype.Repository

@Repository
interface ContentRepo : BaseMongoRepo<Content> {
}