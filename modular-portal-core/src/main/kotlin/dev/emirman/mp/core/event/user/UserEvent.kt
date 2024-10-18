package dev.emirman.mp.core.event.user

import dev.emirman.mp.core.model.user.User
import org.springframework.context.ApplicationEvent

abstract class UserEvent(user: User) : ApplicationEvent(user)
class NewUserEvent(val user: User) : UserEvent(user)
