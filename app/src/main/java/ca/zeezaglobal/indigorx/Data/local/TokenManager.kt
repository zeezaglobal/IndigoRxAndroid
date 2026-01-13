package ca.zeezaglobal.indigorx.Data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ca.zeezaglobal.indigorx.Domain.modal.User
import ca.zeezaglobal.indigorx.Domain.modal.UserStatus
import ca.zeezaglobal.indigorx.Domain.modal.UserType

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "indigorx_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_TYPE_KEY = stringPreferencesKey("user_type")
        private val USER_STATUS_KEY = stringPreferencesKey("user_status")
        private val USER_SPECIALIZATION_KEY = stringPreferencesKey("user_specialization")
        private val USER_LICENSE_KEY = stringPreferencesKey("user_license")
        private val USER_PROFILE_COMPLETE_KEY = booleanPreferencesKey("user_profile_complete")
        private val USER_EMAIL_VERIFIED_KEY = booleanPreferencesKey("user_email_verified")
        private val USER_VALIDATED_KEY = booleanPreferencesKey("user_validated")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.id
            preferences[USER_NAME_KEY] = user.name
            preferences[USER_EMAIL_KEY] = user.email
            preferences[USER_TYPE_KEY] = user.userType.name
            preferences[USER_STATUS_KEY] = user.status.name
            preferences[USER_SPECIALIZATION_KEY] = user.specialization ?: ""
            preferences[USER_LICENSE_KEY] = user.licenseNumber ?: ""
            preferences[USER_PROFILE_COMPLETE_KEY] = user.isProfileComplete
            preferences[USER_EMAIL_VERIFIED_KEY] = user.isEmailVerified
            preferences[USER_VALIDATED_KEY] = user.isValidated
        }
    }

    suspend fun getUser(): User? {
        return context.dataStore.data.map { preferences ->
            val id = preferences[USER_ID_KEY] ?: return@map null
            val name = preferences[USER_NAME_KEY] ?: return@map null
            val email = preferences[USER_EMAIL_KEY] ?: return@map null

            User(
                id = id,
                name = name,
                email = email,
                userType = UserType.fromString(preferences[USER_TYPE_KEY] ?: ""),
                status = UserStatus.fromString(preferences[USER_STATUS_KEY] ?: ""),
                specialization = preferences[USER_SPECIALIZATION_KEY]?.takeIf { it.isNotEmpty() },
                licenseNumber = preferences[USER_LICENSE_KEY]?.takeIf { it.isNotEmpty() },
                isProfileComplete = preferences[USER_PROFILE_COMPLETE_KEY] ?: false,
                isEmailVerified = preferences[USER_EMAIL_VERIFIED_KEY] ?: false,
                isValidated = preferences[USER_VALIDATED_KEY] ?: false
            )
        }.first()
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(USER_NAME_KEY)
            preferences.remove(USER_EMAIL_KEY)
            preferences.remove(USER_TYPE_KEY)
            preferences.remove(USER_STATUS_KEY)
            preferences.remove(USER_SPECIALIZATION_KEY)
            preferences.remove(USER_LICENSE_KEY)
            preferences.remove(USER_PROFILE_COMPLETE_KEY)
            preferences.remove(USER_EMAIL_VERIFIED_KEY)
            preferences.remove(USER_VALIDATED_KEY)
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}