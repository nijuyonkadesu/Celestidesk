package one.njk.celestidesk.network

import one.njk.celestidesk.network.auth.model.AuthLoginRequest
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest
import one.njk.celestidesk.network.auth.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiService {

    // Auth Endpoints starts -----------------------
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
    // Auth Endpoints ends --------------------------

    // Other Endpoints
    @GET("api/request/pending")
    suspend fun getPendingRequests(
        @Header("Authorization") token: String
    ): NetworkPendingRequestContainer
}