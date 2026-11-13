package com.example.aplicacionpeluquerias.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionpeluquerias.R
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> =_uiState.asStateFlow()

    private val _uiEvent = Channel<RegisterUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onNameChanged(nombre:String){
        _uiState.update { currentState ->
            val nombreError = validateName(nombre)
            currentState.copy(
                nombre = nombre,
                nombreError = nombreError,
                isRegisterEnabled = isFormValid(
                    nombre, currentState.email, currentState.password, currentState.telefono,
                    nombreError, currentState.emailError, currentState.passwordError, currentState.telefonoError
                )
            )
        }
    }

    fun onEmailChanged(email:String){
        _uiState.update { currentState ->
            val emailError = validateEmail(email)
            currentState.copy(
                email = email,
                emailError = emailError,
                isRegisterEnabled = isFormValid(
                    currentState.nombre, email, currentState.password, currentState.telefono,
                    currentState.nombreError, emailError, currentState.passwordError, currentState.telefonoError
                )
            )
        }
    }

    fun onTelefonoChanged(telefono: String) {
        _uiState.update { currentState ->
            val telefonoError = validateTelefono(telefono)
            currentState.copy(
                telefono = telefono,
                telefonoError = telefonoError,
                isRegisterEnabled = isFormValid(
                    currentState.nombre, currentState.email, currentState.password, telefono,
                    currentState.nombreError, currentState.emailError, currentState.passwordError, telefonoError
                )
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            val passwordError = validatePassword(password)
            currentState.copy(
                password = password,
                passwordError = passwordError,
                isRegisterEnabled = isFormValid(
                    currentState.nombre, currentState.email, password, currentState.telefono,
                    currentState.nombreError, currentState.emailError, passwordError, currentState.telefonoError
                )
            )
        }
    }

    fun onRegisterClicked(){
        val currentState = _uiState.value

        if(!currentState.isRegisterEnabled||currentState.isRegister)return

        viewModelScope.launch {
            _uiState.update { it.copy(isRegister = true) }
            kotlinx.coroutines.delay(1500) // Simula guardado en servidor
            _uiState.update { it.copy(isRegister = false) }
            _uiEvent.send(RegisterUiEvent.NavigationToLogin)
        }
    }

    private fun validateName(name: String): Int?{
        return when {
            name.isEmpty() -> null // No mostrar error si está vacío al escribir
            name.isBlank() -> R.string.error_empty_name
            else -> null
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isEmpty() -> null
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.error_invalid_email
            else -> null
        }
    }

    private fun validateTelefono(telefono: String): Int? {
        return when {
            telefono.isEmpty() -> null
            telefono.length < 9 || !telefono.all { it.isDigit() } -> R.string.error_invalid_phone
            else -> null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isEmpty() -> null
            password.length < 6 -> R.string.error_password_too_short
            else -> null
        }
    }

    private fun isFormValid(
        nombre: String,
        email: String,
        password: String,
        telefono: String,

        nombreError: Int?,
        emailError: Int?,
        passwordError: Int?,
        telefonoError: Int?
    ): Boolean{
        return nombre.isNotBlank()&& email.isNotBlank()&&
                password.isNotBlank() && telefono.isNotBlank() &&
                nombreError==null && emailError==null && passwordError==null &&
                telefonoError==null
    }


}