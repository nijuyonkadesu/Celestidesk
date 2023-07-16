package one.njk.celestidesk.network.auth

import one.njk.celestidesk.network.auth.model.AuthLoginRequest
import one.njk.celestidesk.network.auth.model.AuthResult
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest

interface AuthRepository {
    suspend fun signUp(user: AuthSignUpRequest): AuthResult<Unit>
    suspend fun logIn(user: AuthLoginRequest): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}