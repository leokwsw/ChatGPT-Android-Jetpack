package dev.leonardpark.chatgpt.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dev.leonardpark.chatgpt.store.datastore.SettingsDataStore
import dev.leonardpark.chatgpt.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    application: Application
): BaseViewModel(application) {

    var apiKey by mutableStateOf("")

    fun saveApiKey() = viewModelScope.launch {
        settingsDataStore.saveApiKey(apiKey)
    }
}