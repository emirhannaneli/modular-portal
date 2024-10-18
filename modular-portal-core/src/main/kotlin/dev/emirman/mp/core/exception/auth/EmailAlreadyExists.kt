package dev.emirman.mp.core.exception.auth

import net.lubble.util.exception.AlreadyExistsException

class EmailAlreadyExists(email: String) : AlreadyExistsException("email", email) {
}