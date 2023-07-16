package one.njk.celestidesk.data.auth

import one.njk.celestidesk.data.auth.model.AuthLoginRequest
import one.njk.celestidesk.data.auth.model.AuthSignUpRequest
import one.njk.celestidesk.data.auth.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface AuthApi {

    @POST("api/employee/signup")
    suspend fun signUp(
        @Body request: AuthSignUpRequest
    ): TokenResponse

    @POST("api/employee/login")
    suspend fun logIn(
        @Body request: AuthLoginRequest
    ): TokenResponse

    @GET("checkauth")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}