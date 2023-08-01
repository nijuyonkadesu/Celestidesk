package one.njk.celestidesk.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import one.njk.celestidesk.network.auth.model.TokenResponse

enum class Role {
    EMPLOYEE, TEAM_LEAD, MANAGER
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
        return role?.let { Role.valueOf(it) } ?: Role.EMPLOYEE
    }

    suspend fun getToken(): TokenResponse {
        val role = context.rolesDataStore.data.first()[ROLE] ?: Role.EMPLOYEE.toString()
        return TokenResponse(
            "local copy",
            context.rolesDataStore.data.first()[TOKEN] ?: "",
            Role.valueOf(role)
        )
    }

    suspend fun setToken(tokenResponse: TokenResponse) {
        setRole(tokenResponse.role)
        context.rolesDataStore.edit {
            it[TOKEN] = tokenResponse.token
        }
    }
}