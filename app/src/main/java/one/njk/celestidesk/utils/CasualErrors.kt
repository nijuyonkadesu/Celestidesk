package one.njk.celestidesk.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import one.njk.celestidesk.network.auth.model.AuthResult
import retrofit2.HttpException

suspend fun failsafe(block: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            block()
        } catch (e: HttpException) {
            Log.d("network", "${e.message}")

        } catch (e: Exception){
            Log.d("network", "fatal: ${e.message}")
        }
    }
}

suspend fun failsafeAuth(block: suspend () -> Unit): AuthResult<Unit> {
    return withContext(Dispatchers.IO) {
        try {
            block()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            Log.d("network", e.message.toString())

            if(e.code() == 401) AuthResult.UnAuthorized()
            else AuthResult.UnknownError()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}

/*
* TODO: Another failsafe function
*  - See exact HTTP error codes and return something in sealed class, even if something fails
*  - Show it as a snackbar (with try again button on failure)
*  - don't allow dismissing bottom sheet while loading ...
*  - ah, Show a indefinite linear progress bar at the bottom
*  - Dismiss Bottom sheet after successful creation, and refresh the main screen
*  - may include a swipe to refresh layout in main screen
* */