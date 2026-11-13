package com.example.aplicacionpeluquerias.viewmodels

sealed interface RecuperarUiEvent {
    object NavigateToLogin : RecuperarUiEvent
    data class ShowMessage(val messageResId: Int) : RecuperarUiEvent
}