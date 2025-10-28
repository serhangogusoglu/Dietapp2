package com.example.diet_app.LandingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {
    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners(){
        binding.buttonLogin.setOnClickListener {
            viewModel.onLoginClicked()
        }

        binding.textSignup.setOnClickListener {
            viewModel.onSignUpClicked()
        }
    }

    private fun setupObservers(){
        viewModel.navigateToLogin.observe(viewLifecycleOwner) { navigate ->
            if(navigate) {
                findNavController().navigate(R.id.action_landingFragment_to_loginFragment)
                viewModel.navigationComplete()
            }
        }

        viewModel.navigateToSignUp.observe(viewLifecycleOwner) {navigate ->
            if(navigate) {
                findNavController().navigate(R.id.action_landingFragment_to_signupFragment)
                viewModel.navigationComplete()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Bellek sızıntısını önlemek için View Binding değişkenini null yap
        _binding = null
    }
}

