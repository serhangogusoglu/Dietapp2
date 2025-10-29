package com.example.diet_app.SignUpFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diet_app.R
import com.example.diet_app.data.database.AppDatabase
import com.example.diet_app.data.repository.UserRepository
import com.example.diet_app.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment(){

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = UserRepository(database.userDao())
        SignupViewModel.Companion.Factory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInputBinding()
        setupListeners()
        setupObservers()
    }

    private fun setupInputBinding() {
        binding.inputEmail.addTextChangedListener(createWatcher { text ->
            viewModel.email.value = text
        })

        binding.inputPassword.addTextChangedListener(createWatcher { text ->
            viewModel.password.value = text
        })

        binding.inputConfirmPassword.addTextChangedListener(createWatcher {text ->
            viewModel.confirmPassword.value = text
        })
    }

    private fun createWatcher(afterTextChanged: (String) -> Unit) : TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        }
    }

    private fun setupListeners() {
        binding.buttonSignup.setOnClickListener {
            viewModel.onSignupClicked()
        }
        binding.imageBack.setOnClickListener {
            viewModel.onBackClicked()
        }
        binding.textLogin.setOnClickListener {
            viewModel.onLoginTextClicked()
        }
    }

    private fun setupObservers(){
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if(message != null) {
                binding.textError.text = message
                binding.textError.visibility = View.VISIBLE
            } else {
                binding.textError.visibility = View.GONE
            }
        }

        viewModel.registrationSuccess.observe(viewLifecycleOwner) { success ->
            if(success) {
                Toast.makeText(context, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                viewModel.operationComplete()
            }
        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner) { navigate ->
            if(navigate) {
                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                viewModel.operationComplete()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}