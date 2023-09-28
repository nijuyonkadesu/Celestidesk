package one.njk.celestidesk.network.auth.model

import com.squareup.moshi.Json
import one.njk.celestidesk.database.Role

data class AuthSignUpRequest(
    val name: String,
    val username: String,
    @Json(name = "orghandle") val orgHandle: String,
    val type: Role,
    val password: String,
)
// TODO: Nuke signup discussion