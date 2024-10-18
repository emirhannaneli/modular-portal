package dev.emirman.mp.core.event.user

import dev.emirman.mp.core.model.user.User
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class UserEventPublisher(val publisher: ApplicationEventPublisher) {
    fun publishNewUserEvent(user: User) {
        publisher.publishEvent(NewUserEvent(user))
    }
}