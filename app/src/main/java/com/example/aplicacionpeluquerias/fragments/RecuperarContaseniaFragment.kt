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
import com.example.aplicacionpeluquerias.databinding.FragmentRecuperarContaseniaBinding
import com.example.aplicacionpeluquerias.viewmodels.RecuperarUiEvent
import com.example.aplicacionpeluquerias.viewmodels.RecuperarViewModel
import kotlinx.coroutines.launch


class RecuperarContaseniaFragment : Fragment() {

    private lateinit var binding: FragmentRecuperarContaseniaBinding
    private val viewModel: RecuperarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecuperarContaseniaBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Escuchar cambios de estado de la UI
                launch {
                    viewModel.uiState.collect { state ->
                        // Actualizar errores en TextInputLayouts
                        binding.contenedorTelefonoRecuperacion.error =
                            state.telefonoError?.let { getString(it) }
                        binding.contenedorCodigoRecuperar.error =
                            state.codigoError?.let { getString(it) }

                        // Habilitar / deshabilitar botones
                        binding.botonEnviarTelefono.isEnabled =
                            state.isSendSmsEnabled && !state.isLoading
                        binding.botonEnviarCodigo.isEnabled =
                            state.isSendCodeEnabled && !state.isLoading
                    }
                }

                // Escuchar eventos de un solo uso
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {

                            is RecuperarUiEvent.ShowMessage -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(event.messageResId),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is RecuperarUiEvent.NavigateToLogin -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.msg_recover_success),
                                    Toast.LENGTH_SHORT
                                ).show()

                                val navHostFragment = requireActivity().supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                                val navController = navHostFragment.navController

                                navController.navigate(R.id.action_recuperar_to_login)
                            }
                        }
                    }
                }
            }
        }

        binding.entradaTelefonoRecuperar.doOnTextChanged { text, _, _, _ ->
            viewModel.onTelefonoChanged(text.toString())
        }

        binding.botonEnviarTelefono.setOnClickListener {
            viewModel.onSendSmsClicked()
        }

        binding.entradaCodigoRecuperar.doOnTextChanged { text, _, _, _ ->
            viewModel.onCodigoChanged(text.toString())
        }

        binding.botonEnviarCodigo.setOnClickListener {
            viewModel.onSendCodeClicked()
        }
    }



}