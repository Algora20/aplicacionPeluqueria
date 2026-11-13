package com.example.aplicacionpeluquerias.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionpeluquerias.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecuperarViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RecuperarUiState())
    val uiState: StateFlow<RecuperarUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<RecuperarUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onTelefonoChanged(telefono: String) {
        _uiState.update { currentState ->
            val telefonoError = validateTelefono(telefono)
            currentState.copy(
                telefono = telefono,
                telefonoError = telefonoError,
                isSendSmsEnabled = isPhoneValid(telefono,telefonoError)
            )
        }
    }

    fun onCodigoChanged(codigo: String) {
        _uiState.update { currentState ->
            val codigoError = validateCodigo(codigo)
            currentState.copy(
                codigo = codigo,
                codigoError = codigoError,
                isSendCodeEnabled = isCodeValid(codigo, codigoError)
            )
        }
    }

    fun onSendSmsClicked() {
        val currentState = _uiState.value
        if (!currentState.isSendSmsEnabled || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000) // Simulación de envío de SMS
            _uiState.update { it.copy(isLoading = false) }
            _uiEvent.send(RecuperarUiEvent.ShowMessage(R.string.msg_sms_sent))
        }
    }

    fun onSendCodeClicked() {
        val currentState = _uiState.value
        if (!currentState.isSendCodeEnabled || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1500) // Simulación de comprobación del código
            _uiState.update { it.copy(isLoading = false) }
            _uiEvent.send(RecuperarUiEvent.NavigateToLogin)
        }
    }

    private fun validateTelefono(telefono: String): Int? {
        return when {
            telefono.isEmpty() -> null
            telefono.length < 9 || !telefono.all { it.isDigit() } -> R.string.error_invalid_phone
            else -> null
        }
    }

    private fun validateCodigo(codigo: String): Int? {
        return when {
            codigo.isEmpty() -> null
            codigo.length < 4 -> R.string.error_invalid_code
            else -> null
        }
    }

    private fun isPhoneValid(
        telefono: String,
        telefonoError: Int?
    ): Boolean {
        return telefono.isNotBlank() && telefonoError == null
    }

    private fun isCodeValid(
        codigo: String,
        codigoError: Int?
    ): Boolean {
        return codigo.isNotBlank() && codigoError == null
    }
}