package dev.leonardpark.chatgpt.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dev.leonardpark.chatgpt.store.datastore.SettingsDataStore
import dev.leonardpark.chatgpt.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    application: Application
): BaseViewModel(application) {

    var endpoint by mutableStateOf("")
    var apiKey by mutableStateOf("")
    var model by mutableStateOf("")
    var temperature by mutableStateOf("")

    var changesMade by mutableStateOf(false)

    init {
        viewModelScope.launch {
            endpoint = settingsDataStore.endpoint.first()
            apiKey = settingsDataStore.apiKey.first() ?: ""
            model = settingsDataStore.model.first()
            temperature = settingsDataStore.temperature.first().toString()
        }
    }

    fun save(onSaved: () -> Unit) = viewModelScope.launch {
        settingsDataStore.saveEndpoint(endpoint)
        settingsDataStore.saveApiKey(apiKey)
        settingsDataStore.saveModel(model)
        settingsDataStore.saveTemperature(
            temperature
                .replace(",", ".")
                .toFloatOrNull()
                ?.coerceIn(0f, 2f) ?: 1f
        )

        onSaved()
    }
}