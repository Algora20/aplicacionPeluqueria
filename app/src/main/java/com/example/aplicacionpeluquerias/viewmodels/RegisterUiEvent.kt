package com.example.aplicacionpeluquerias.viewmodels

sealed interface RegisterUiEvent {
    object NavigationToLogin: RegisterUiEvent
    data class ShowError(val messageResId: Int) : RegisterUiEvent
}