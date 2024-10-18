package dev.emirman.mp.core.model.content

import net.lubble.util.model.BaseMongoModel
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "contents")
class Content : BaseMongoModel() {
    var text: String? = null

    open class Params : SearchParams() {}
}