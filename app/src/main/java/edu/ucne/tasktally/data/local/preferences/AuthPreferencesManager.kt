package edu.ucne.tasktally.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class AuthPreferencesManager @Inject constructor(
    private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val TOKEN_EXPIRES_AT_KEY = stringPreferencesKey("token_expires_at")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val MENTOR_ID_KEY = intPreferencesKey("mentor_id")
        private val GEMA_ID_KEY = intPreferencesKey("gema_id")
        private val ZONE_ID_KEY = intPreferencesKey("zone_id")
        private val PUNTOS_DISPONIBLES_KEY = intPreferencesKey("puntos_disponibles")
        private val PUNTOS_GASTADOS_KEY = intPreferencesKey("puntos_gastados")
    }

    suspend fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        userId: Int,
        username: String,
        email: String?,
        expiresAt: String?,
        role: String? = null,
        mentorId: Int? = null,
        gemaId: Int? = null,
        zoneId: Int? = null,
        puntosDisponibles: Int? = null,
        puntosGastados: Int? = null
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[USER_ID_KEY] = userId
            preferences[USERNAME_KEY] = username
            preferences[EMAIL_KEY] = email ?: ""
            preferences[IS_LOGGED_IN_KEY] = true
            preferences[TOKEN_EXPIRES_AT_KEY] = expiresAt ?: ""
            preferences[ROLE_KEY] = role ?: ""
            if (mentorId != null) preferences[MENTOR_ID_KEY] = mentorId
            if (gemaId != null) preferences[GEMA_ID_KEY] = gemaId
            if (zoneId != null) preferences[ZONE_ID_KEY] = zoneId
            if (puntosDisponibles != null) preferences[PUNTOS_DISPONIBLES_KEY] = puntosDisponibles
            if (puntosGastados != null) preferences[PUNTOS_GASTADOS_KEY] = puntosGastados
        }
    }

    suspend fun updateTokens(
        accessToken: String,
        refreshToken: String,
        expiresAt: String?
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[TOKEN_EXPIRES_AT_KEY] = expiresAt ?: ""
        }
    }

    suspend fun clearAuthData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN_KEY]
    }

    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val username: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY]
    }

    val email: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    val tokenExpiresAt: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_EXPIRES_AT_KEY]
    }

    val role: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ROLE_KEY]
    }

    val mentorId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[MENTOR_ID_KEY]
    }

    val gemaId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[GEMA_ID_KEY]
    }

    val zoneId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[ZONE_ID_KEY]
    }

    val puntosDisponibles: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[PUNTOS_DISPONIBLES_KEY]
    }

    val puntosGastados: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[PUNTOS_GASTADOS_KEY]
    }

    suspend fun updatePuntos(puntosDisponibles: Int, puntosGastados: Int) {
        dataStore.edit { preferences ->
            preferences[PUNTOS_DISPONIBLES_KEY] = puntosDisponibles
            preferences[PUNTOS_GASTADOS_KEY] = puntosGastados
        }
    }
}