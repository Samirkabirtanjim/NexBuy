package com.example.nexbuy.viewmodel.repo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DatastoreManager(private val context: Context) {

    // Define a key for the token
    private val USER_TOKEN_KEY = stringPreferencesKey("user_token")

    // Save the token in DataStore
    suspend fun saveString(key: String, value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    // Specific method to save user token
    suspend fun saveUserToken(token: String) {
        saveString("user_token", token)
    }

    // Retrieve the token from DataStore
    fun getString(key: String, defaultValue: String): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: defaultValue
            }
    }

    // Specific method to get user token
    fun getUserToken(): Flow<String?> {
        return getString("user_token", "")
    }

    // Clear user token method
    suspend fun clearUserToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN_KEY)
        }
    }
}