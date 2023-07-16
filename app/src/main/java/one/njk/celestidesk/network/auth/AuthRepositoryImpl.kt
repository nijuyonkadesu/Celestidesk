package one.njk.celestidesk.network.auth

import android.util.Log
import one.njk.celestidesk.data.RolesDataStore
import one.njk.celestidesk.network.auth.model.AuthLoginRequest
import one.njk.celestidesk.network.auth.model.AuthResult
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest
import one.njk.celestidesk.network.ApiService
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: ApiService,
    private val pref: RolesDataStore
): AuthRepository {
    override suspend fun signUp(user: AuthSignUpRequest): AuthResult<Unit> {
        return try {
            val response = api.signUp(user)
            pref.setToken(response)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            Log.d("network", e.message.toString())

            if(e.code() == 401) AuthResult.UnAuthorized()
            else AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun logIn(user: AuthLoginRequest): AuthResult<Unit> {
        return try {
            val response = api.logIn(user)
            pref.setToken(response)
            Log.d("network", response.message)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            Log.d("network", e.message.toString())

            if(e.code() == 401) AuthResult.UnAuthorized()
            else AuthResult.UnknownError()
        } catch (e: Exception) {
            Log.d("network", e.message.toString())
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            // Trying out authenticate directly when not signed up case:
            val token = pref.getToken()
            api.authenticate("Bearer ${token.token}")
            AuthResult.Authorized()

        } catch (e: HttpException){
            if(e.code() == 401) AuthResult.UnAuthorized()
            else AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}
