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

// TODO: add below fields - but ignore while sending actual request
// TODO: [imp] Discuss about: **New SignUp requests need approval from 'ADMIN', once he has approved, he can LogIn **
/**
    --------
    - EMAIL
    - ROOM ID
    - Parent Number
    --------
 */