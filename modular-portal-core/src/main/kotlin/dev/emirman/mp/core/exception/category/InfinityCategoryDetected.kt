package dev.emirman.mp.core.exception.category

import net.lubble.util.model.ExceptionModel
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST

class InfinityCategoryDetected : RuntimeException(), ExceptionModel {
    override fun code(): String {
        return "0x000"
    }

    override fun message(): String {
        return "category.exception.infinity.category.detected"
    }

    override fun status(): HttpStatus {
        return BAD_REQUEST
    }
}