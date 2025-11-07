package com.example.diet_app.CalorieResultFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentCalorieResultBinding

class CalorieResultFragment : Fragment() {

    private var _binding: FragmentCalorieResultBinding? = null
    private val binding get() = _binding!!

    private val args: CalorieResultFragmentArgs by navArgs()

    private lateinit var viewModel: CalorieResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalorieResultBinding.inflate(inflater, container, false)

        val factory = CalorieResultViewModelFactory(
            args.height,
            args.weight,
            args.age,
            args.gender,
            args.activityLevel
        )
        viewModel = ViewModelProvider(this, factory).get(CalorieResultViewModel::class.java)

        setupObservers()
        setupListeners()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.maintenanceKcal.observe(viewLifecycleOwner) {kcal ->
            binding.textKcalResult.text = getString(R.string.kcal_display, kcal)
        }
    }

    private fun setupListeners() {
        binding.buttonFinishRegistration.setOnClickListener {
            val action = CalorieResultFragmentDirections.actionCalorieResultFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}