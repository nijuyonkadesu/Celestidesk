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
    // Pending for Everyone
    @GET("api/request/pending")
    suspend fun getPendingRequests(
        @Header("Authorization") token: String
    ): NetworkPendingRequestContainer

    @POST("api/transaction/decide")
    suspend fun makeDecision(
        @Header("Authorization") token: String,
        @Body decisionRequest: DecisionRequest
    ): Message

    // History for TeamLead and Manager to see past Transactions
    @GET("api/transaction/history")
    suspend fun getPastTransactions(
        @Header("Authorization") token: String,
    ): NetworkTransactionsContainer

    @POST("api/request/create")
    suspend fun createNewRequest(
        @Header("Authorization") token: String,
        @Body request: NetworkNewRequest
    ): Message
    // TODO: Wrap message with sealed class
}
