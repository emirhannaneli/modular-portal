package dev.emirman.mp.core.service.content

import dev.emirman.mp.core.model.content.Content
import dev.emirman.mp.core.repo.mongo.content.ContentRepo
import net.lubble.util.exception.NotFoundException
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class IContentService(val repo: ContentRepo) : ContentService {
    override fun save(content: Content): Content {
        return repo.save(content)
    }

    override fun create(text: String): ObjectId {
        val content = Content()
        content.text = text
        return save(content).id
    }

    override fun findById(id: String): Content {
        return repo.findById(ObjectId(id)).orElseThrow { NotFoundException("id", id) }
    }

    override fun update(content: Content, text: String): ObjectId {
        content.text = text
        return save(content).id
    }

    override fun delete(content: Content) {
        repo.delete(content)
    }


}