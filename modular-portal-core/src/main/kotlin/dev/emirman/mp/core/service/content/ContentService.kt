package dev.emirman.mp.core.service.content

import dev.emirman.mp.core.model.content.Content
import org.bson.types.ObjectId

interface ContentService {
    fun save(content: Content): Content
    fun create(text: String): ObjectId

    fun findById(id: String): Content

    fun update(content: Content, text: String): ObjectId

    fun delete(content: Content)
}