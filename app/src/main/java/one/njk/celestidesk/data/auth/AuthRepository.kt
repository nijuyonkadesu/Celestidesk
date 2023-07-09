package one.njk.celestidesk.data.auth

import one.njk.celestidesk.data.auth.model.AuthLoginRequest
import one.njk.celestidesk.data.auth.model.AuthResult
import one.njk.celestidesk.data.auth.model.AuthSignUpRequest

interface AuthRepository {
    suspend fun signUp(user: AuthSignUpRequest): AuthResult<Unit>
    suspend fun logIn(user: AuthLoginRequest): AuthResult<Unit>
}