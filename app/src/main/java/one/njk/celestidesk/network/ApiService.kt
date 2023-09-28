package one.njk.celestidesk.network

import one.njk.celestidesk.network.auth.model.AuthLoginRequest
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest
import one.njk.celestidesk.network.auth.model.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiService {

    // ----------------------- Auth Endpoints starts -----------------------
    @POST("api/employee/signup")
    suspend fun signUp(
        @Body request: AuthSignUpRequest
    ): AuthResponse

    @POST("api/employee/login")
    suspend fun logIn(
        @Body request: AuthLoginRequest
    ): AuthResponse

    @GET("checkauth")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
    // ----------------------- Auth Endpoints ends --------------------------

    /**
     * To get requests in any [Stage]. Used by all [one.njk.celestidesk.database.Role]
     */
    @GET("api/request/pending")
    suspend fun getPendingRequests(
        @Header("Authorization") token: String
    ): NetworkPendingRequestContainer

    @POST("api/transaction/decide")
    suspend fun makeDecision(
        @Header("Authorization") token: String,
        @Body decisionRequest: DecisionRequest
    ): Message

    /**
     * for TeamLead and Manager to see past Transactions
     */
    @GET("api/transaction/history")
    suspend fun getPastTransactions(
        @Header("Authorization") token: String,
    ): NetworkTransactionsContainer

    /**
     * Create a new request, with an applicable [Decision].
     */
    @POST("api/request/create")
    suspend fun createNewRequest(
        @Header("Authorization") token: String,
        @Body request: NetworkNewBreakRequest
    ): Message
    // TODO: Wrap message with sealed class
}
