package one.njk.celestidesk.database

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import one.njk.celestidesk.network.auth.model.AuthResponse

enum class Role {
    EMPLOYEE, TEAM_LEAD, MANAGER, EMERGENCY
}

class RolesDataStore(private val context: Context) {

    companion object {
        private val Context.rolesDataStore by preferencesDataStore(name = "roles")
        private val ROLE = stringPreferencesKey("ROLE")
        private val TOKEN = stringPreferencesKey("TOKEN")
    }

    private suspend fun setRole(role: Role) {
        context.rolesDataStore.edit {
            it[ROLE] = role.toString()
        }
    }

    suspend fun getRole(): Role {
        val role = context.rolesDataStore.data.first()[ROLE]
        Log.d("pref", "$role")
        return role?.let { Role.valueOf(it) } ?: Role.EMPLOYEE
    }

    suspend fun getToken(): AuthResponse {
        val role = context.rolesDataStore.data.first()[ROLE] ?: Role.EMPLOYEE.toString()
        return AuthResponse(
            "local copy",
            context.rolesDataStore.data.first()[TOKEN] ?: "",
            Role.valueOf(role)
        )
    }

    // TODO: Refactor TokenResponse name
    suspend fun setToken(authResponse: AuthResponse) {
        Log.d("pref", "saved: ${authResponse.role}")
        setRole(authResponse.role)
        context.rolesDataStore.edit {
            it[TOKEN] = authResponse.token
        }
    }

    suspend fun format() {
        context.rolesDataStore.edit {
            it[ROLE] = Role.EMPLOYEE.toString()
            it[TOKEN] = ""
        }
    }
}