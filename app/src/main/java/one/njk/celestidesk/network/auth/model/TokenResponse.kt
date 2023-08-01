package one.njk.celestidesk.network.auth.model

import one.njk.celestidesk.database.Role

data class TokenResponse (
    val message: String,
    val token: String,
    val role: Role
)