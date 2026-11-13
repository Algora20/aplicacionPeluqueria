package com.example.aplicacionpeluquerias.viewmodels

data class RecuperarUiState(
    val telefono: String = "",
    val telefonoError: Int? = null,
    val isSendSmsEnabled: Boolean = false,
    val codigo: String = "",
    val codigoError: Int? = null,
    val isSendCodeEnabled: Boolean = false,
    val isLoading: Boolean = false
)