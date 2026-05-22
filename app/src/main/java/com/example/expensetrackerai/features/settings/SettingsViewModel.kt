package com.example.expensetrackerai.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    fun onCurrencyChange(currency: String) {
        _settingsState.value = _settingsState.value.copy(selectedCurrency = currency)
    }

    fun onThemeChange(isDarkMode: Boolean) {
        _settingsState.value = _settingsState.value.copy(isDarkMode = isDarkMode)
    }
}

data class SettingsState(
    val selectedCurrency: String = "USD",
    val isDarkMode: Boolean = false,
    val isBiometricEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true
)
