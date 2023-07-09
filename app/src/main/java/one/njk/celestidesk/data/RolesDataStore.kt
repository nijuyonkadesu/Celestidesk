package one.njk.celestidesk.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

enum class Role {
    EMPLOYEE, TEAM_LEAD, MANAGER
}

class RolesDataStore(private val context: Context) {

    companion object {
        private val Context.rolesDataStore by preferencesDataStore(name = "roles")
        private val ROLE = stringPreferencesKey("ROLE")
    }

    suspend fun setRole(role: Role) {
        context.rolesDataStore.edit {
            it[ROLE] = role.toString()
        }
    }

    suspend fun getRole(): Role {
        val role = context.rolesDataStore.data.first()[ROLE]
        return role?.let { Role.valueOf(it) } ?: Role.EMPLOYEE
    }
}