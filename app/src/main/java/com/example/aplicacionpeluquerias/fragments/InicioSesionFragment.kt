package com.example.aplicacionpeluquerias.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.aplicacionpeluquerias.R
import com.example.aplicacionpeluquerias.databinding.FragmentInicioSesionBinding
import com.example.aplicacionpeluquerias.viewmodels.LoginUiEvent
import com.example.aplicacionpeluquerias.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

class InicioSesionFragment : Fragment() {

    private lateinit var binding: FragmentInicioSesionBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInicioSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Observadores de estado y eventos
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.contenedorCorreoInicioSesion.error =
                            state.emailError?.let { getString(it) }
                        binding.contenedorContraseAInicioSesion.error =
                            state.passwordError?.let { getString(it) }

                        binding.botonInicioSesion.isEnabled =
                            state.isLoginEnabled && !state.isLoading
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is LoginUiEvent.NavigateToHome -> {
                                val navHostFragment = requireActivity().supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                                navHostFragment.navController.navigate(R.id.action_login_to_home)
                            }

                            is LoginUiEvent.ShowError -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(event.messageResId),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        // 2. Listeners de texto y clics
        binding.entradaEmailInicioSesion.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.entradaContraseAInicioSesion.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.botonInicioSesion.setOnClickListener {
            viewModel.onLoginClicked()
        }

        binding.textoCrearCuenta.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navHostFragment.navController.navigate(R.id.action_login_to_registro)
        }

        binding.textoOlividarContrasenia.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navHostFragment.navController.navigate(R.id.action_login_to_recuperar)
        }
    }
}