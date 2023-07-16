package one.njk.celestidesk.network.auth.model

import one.njk.celestidesk.data.Role

data class AuthSignUpRequest(
    val name: String,
    val username: String,
    val orgHandle: String,
    val type: Role,
    val password: String,
)