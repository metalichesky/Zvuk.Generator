package com.metalichesky.zvuk.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class UserPrefsDataSource(
    private val context: Context
) {

    val defaultAlphabetId: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[DEFAULT_ALPHABET_ID_KEY] ?: ENGLISH_ALPHABET_ID
    }

    suspend fun setDefaultAlphabetId(id: Long) {
        context.dataStore.edit { prefs ->
            prefs[DEFAULT_ALPHABET_ID_KEY] = id
        }
    }

    suspend fun getDefaultAlphabetId(): Long {
        return context.dataStore.data.firstOrNull()?.get(DEFAULT_ALPHABET_ID_KEY) ?: ENGLISH_ALPHABET_ID
    }

}


private const val USER_PREFS = "user_prefs"
private const val DEFAULT_ALPHABET_ID = "DEFAULT_ALPHABET_ID"
private val DEFAULT_ALPHABET_ID_KEY = longPreferencesKey(DEFAULT_ALPHABET_ID)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFS)