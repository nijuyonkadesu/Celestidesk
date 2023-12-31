package one.njk.celestidesk.network.auth.model

sealed class AuthResult<T> (val data: T? = null){
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class UnAuthorized<T> : AuthResult<T>()
    class UnknownError<T> : AuthResult<T>()
    class ItsOk<T>: AuthResult<T>()
}
