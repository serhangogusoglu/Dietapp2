package com.example.diet_app.LoginFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diet_app.databinding.FragmentLoginBinding
import com.example.diet_app.R

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputBinding()
        setupListeners()
        setupObservers()
    }

    private fun setupInputBinding(){
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.email.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        })

        binding.inputPassword.addTextChangedListener(object:TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.password.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        })
    }

    private fun setupListeners() {
        binding.buttonLogin.setOnClickListener{
            viewModel.onLoginClicked()
        }

        binding.imageBack.setOnClickListener {
            viewModel.onBackClicked()
        }
    }

    private fun setupObservers(){
        viewModel.errorMessage.observe(viewLifecycleOwner) {message->
            if(message !=null) {
                binding.textError.text = message
                binding.textError.visibility = View.VISIBLE
            }else {
                binding.textError.visibility = View.GONE
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {success ->
            if(success) {
                Toast.makeText(context, "Giriş Başarılı", Toast.LENGTH_SHORT).show()
                viewModel.operationComplete()
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { navigate->
            if(navigate) {
                findNavController().popBackStack()
                viewModel.operationComplete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

