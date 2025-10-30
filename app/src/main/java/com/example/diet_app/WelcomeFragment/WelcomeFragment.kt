package com.example.diet_app.WelcomeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupObservers()
    }

    private fun setupListener() {
        binding.buttonGetStarted.setOnClickListener {
            viewModel.onGetStartedClicked()
        }
    }

    private fun setupObservers() {
        viewModel.navigateToProfile.observe(viewLifecycleOwner){ navigate->
            if(navigate){
                findNavController().navigate(R.id.action_welcomeFragment_to_InfoFragment)
                viewModel.navigationComplete()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}