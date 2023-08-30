package one.njk.celestidesk.network.auth

import android.util.Log
import one.njk.celestidesk.database.RolesDataStore
import one.njk.celestidesk.network.ApiService
import one.njk.celestidesk.network.auth.model.AuthLoginRequest
import one.njk.celestidesk.network.auth.model.AuthResult
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest
import one.njk.celestidesk.utils.failsafeAuth

class AuthRepositoryImpl(
    private val api: ApiService,
    private val pref: RolesDataStore
): AuthRepository {
    override suspend fun signUp(user: AuthSignUpRequest): AuthResult<Unit> =
        failsafeAuth {
            val response = api.signUp(user)
            pref.setToken(response)
        }

    override suspend fun logIn(user: AuthLoginRequest): AuthResult<Unit> =
        failsafeAuth {
            val response = api.logIn(user)
            pref.setToken(response)
            Log.d("network", response.message)
        }

    override suspend fun authenticate(): AuthResult<Unit> =
        failsafeAuth {
            // Trying out authenticate directly when not signed up case:
            val token = pref.getToken()
            api.authenticate("Bearer ${token.token}")
        }
}
