package com.example.aplicacionpeluquerias.viewmodels

interface RegisterUiEvent {
    object NavigationToLogin: RegisterUiEvent
    data class ShowError(val messageResId: Int) : RegisterUiEvent
}