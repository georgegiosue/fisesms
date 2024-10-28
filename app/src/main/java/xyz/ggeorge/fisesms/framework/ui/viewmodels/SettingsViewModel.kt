package xyz.ggeorge.fisesms.framework.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsViewModel(private val application: Application) : AndroidViewModel(application) {

    private val REALTIME_VISION_FEATURE_KEY = booleanPreferencesKey("realtime_vision_feature")
    private val ALIAS_KEY = stringPreferencesKey("alias")
    private val SERVICE_NUMBER_KEY = stringPreferencesKey("service_number")

    val realtimeVisionFeatureEnabled: Flow<Boolean> = application.dataStore.data
        .map { preferences ->
            preferences[REALTIME_VISION_FEATURE_KEY] ?: false
        }

    val alias: Flow<String> = application.dataStore.data
        .map { preferences ->
            preferences[ALIAS_KEY] ?: "ah01"
        }

    val serviceNumber: Flow<String> = application.dataStore.data
        .map { preferences ->
            preferences[SERVICE_NUMBER_KEY] ?: "55555"
        }

    fun toggleRealtimeVisionFeature(enabled: Boolean) {
        viewModelScope.launch {
            application.dataStore.edit { preferences ->
                preferences[REALTIME_VISION_FEATURE_KEY] = enabled
            }
        }
    }

    fun setAlias(alias: String) {
        viewModelScope.launch {
            application.dataStore.edit { preferences ->
                preferences[ALIAS_KEY] = alias
            }
        }
    }

    fun setServiceNumber(serviceNumber: String) {
        viewModelScope.launch {
            application.dataStore.edit { preferences ->
                preferences[SERVICE_NUMBER_KEY] = serviceNumber
            }
        }
    }
}