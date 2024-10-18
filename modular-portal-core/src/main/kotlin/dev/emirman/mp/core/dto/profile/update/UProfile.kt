package dev.emirman.mp.core.dto.profile.update

import net.lubble.util.ReFormat


class UProfile {
    var name: String? = null

    var surname: String? = null

    var email: String? = null

    fun format() {
        name = name?.let { ReFormat(it).trim().format() }
        surname = surname?.let { ReFormat(it).trim().format() }
        email = email?.let { ReFormat(it).trim().lower().format() }
    }
}