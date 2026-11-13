package com.example.aplicacionpeluquerias.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.aplicacionpeluquerias.R
import com.example.aplicacionpeluquerias.databinding.FragmentRegistroBinding
import com.example.aplicacionpeluquerias.viewmodels.RegisterUiEvent
import com.example.aplicacionpeluquerias.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

class RegistroFragment : Fragment() {


    private lateinit var binding: FragmentRegistroBinding
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistroBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val currentState = viewModel.uiState.value
        if (binding.etNombre.text.toString() != currentState.nombre) {
            binding.etNombre.setText(currentState.nombre)
        }
        if (binding.etEmail.text.toString() != currentState.email) {
            binding.etEmail.setText(currentState.email)
        }
        if (binding.etTelefono.text.toString() != currentState.telefono) {
            binding.etTelefono.setText(currentState.telefono)
        }
        if (binding.etPassword.text.toString() != currentState.password) {
            binding.etPassword.setText(currentState.password)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch{
                    viewModel.uiState.collect { state ->
                        // Actualizar errores en TextInputLayouts
                        binding.tilNombre.error = state.nombreError?.let { getString(it) }
                        binding.tilEmail.error = state.emailError?.let { getString(it) }
                        binding.tilTelefono.error = state.telefonoError?.let { getString(it) }
                        binding.tilPassword.error = state.passwordError?.let { getString(it) }

                        // Habilitar / deshabilitar botón
                        binding.buttonCrearCuenta.isEnabled = state.isRegisterEnabled && !state.isRegister
                    }
                }

                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event){
                            is RegisterUiEvent.NavigationToLogin -> {
                                Toast.makeText(requireActivity(), getString(R.string.msg_register_success), Toast.LENGTH_SHORT).show()

                                // Navegar a la pantalla de inicio de sesión
                                val navHostFragment = requireActivity().supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                                val navController = navHostFragment.navController

                                navController.navigate(R.id.action_registro_to_login)
                            }
                        }
                    }
                }
            }
        }

        binding.etNombre.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.etTelefono.doOnTextChanged { text, _, _, _ ->
            viewModel.onTelefonoChanged(text.toString())
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.buttonCrearCuenta.setOnClickListener {
            viewModel.onRegisterClicked()
        }

        binding.textViewIrALogin.setOnClickListener {

            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController

            navController.navigate(R.id.action_registro_to_login)

        }

    }


}