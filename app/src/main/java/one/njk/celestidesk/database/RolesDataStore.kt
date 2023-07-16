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

    suspend fun setRole(role: Role) {
        context.rolesDataStore.edit {
            it[ROLE] = role.toString()
        }
    }

    suspend fun getRole(): Role {
        val role = context.rolesDataStore.data.first()[ROLE]
        return role?.let { Role.valueOf(it) } ?: Role.MANAGER
    }

    suspend fun getToken(): TokenResponse {
        return TokenResponse(
            "local copy",
            context.rolesDataStore.data.first()[TOKEN] ?: ""
        )
    }

    suspend fun setToken(tokenResponse: TokenResponse) {
        context.rolesDataStore.edit {
            it[TOKEN] = tokenResponse.token
        }
    }
}