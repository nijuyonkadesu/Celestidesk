package one.njk.celestidesk.data.auth

import one.njk.celestidesk.data.auth.model.AuthLoginRequest
import one.njk.celestidesk.data.auth.model.AuthSignUpRequest
import one.njk.celestidesk.data.auth.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST


const val BASE_URL = "https://celestidesk.onrender.com/api/employee"
interface AuthApi {

    @POST("signup")
    suspend fun signUp(
        @Body request: AuthSignUpRequest
    ): TokenResponse

    @POST("signin")
    suspend fun signIn(
        @Body request: AuthLoginRequest
    ): TokenResponse

    // TODO: Use interceptor for multiple authentication routes
}