package one.njk.celestidesk.network

sealed class NetworkResult<T> (val data: T? = null) {
    class Success<T>(data: T? = null): NetworkResult<T>(data)
    class Failed<T>(data: T? = null): NetworkResult<T>(data)
    class ItsOk<T>(data: T? = null): NetworkResult<T>(data)
}