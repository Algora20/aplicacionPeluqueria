package com.example.aplicacionpeluquerias.viewmodels

data class RegisterUiState (
    val nombre: String = "",
    val nombreError: Int? = null,
    val email: String = "",
    val emailError: Int? = null,
    val telefono: String = "",
    val telefonoError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null,
    val isRegister: Boolean=false,
    val isRegisterEnabled: Boolean = false
)