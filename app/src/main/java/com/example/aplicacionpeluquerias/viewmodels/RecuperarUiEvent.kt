package com.example.aplicacionpeluquerias.viewmodels

interface RecuperarUiEvent {
    object NavigateToLogin : RecuperarUiEvent
    data class ShowMessage(val messageResId: Int) : RecuperarUiEvent
}