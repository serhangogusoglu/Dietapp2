package com.example.diet_app.TargetFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentTargetBinding

class TargetFragment : Fragment() {

    private var _binding: FragmentTargetBinding? = null
    private val binding get() = _binding!!

    private val args: TargetFragmentArgs by navArgs()

    private val viewModel: TargetViewModel by viewModels {
        TargetViewModelFactory(args.height, args.weight)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTargetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInputBinding()
        setupListeners()
        setupObservers()
    }

    private fun setupInputBinding() {
        //viewmodeldeki livedatayi input alanlarına bagla
        fun bindTextWatcher(editText: EditText, liveData: MutableLiveData<String>) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    liveData.value = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            })
        }

        bindTextWatcher(binding.inputTargetWeight, viewModel.targetWeight)
        bindTextWatcher(binding.inputDurationDays, viewModel.durationDays)
    }

    private fun setupListeners() {
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.unitTargetKg.setOnClickListener { viewModel.onUnitChanged("kg") }
        binding.unitTargetLbs.setOnClickListener { viewModel.onUnitChanged("lbs") }

        binding.buttonLetsSee.setOnClickListener {
            viewModel.onNextClicked()
        }
    }

    private fun setupObservers() {
        // kilo birimi gözlemcisi (ui güncelleme)
        viewModel.weightUnit.observe(viewLifecycleOwner) {unit ->
            updateUnitUI(binding.unitTargetKg, binding.unitTargetLbs, unit, "kg", "lbs")
        }

        viewModel.isNextButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.buttonLetsSee.isEnabled = isEnabled
            binding.buttonLetsSee.alpha = if(isEnabled) 1.0f else 0.5f
        }

        viewModel.healthyRangeText.observe(viewLifecycleOwner) {text ->
            binding.textHealthyRange.text = text
        }

        viewModel.suggestionText.observe(viewLifecycleOwner) { text->
            binding.textSuggestion.text = text
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) {navigate ->
            if(navigate) {
                val action = TargetFragmentDirections.actionTargetFragmentToCongratFragment(
                    height = args.height,
                    weight = args.weight,
                    age = args.age , // ✅ EKLENDİ
                    gender = args.gender, // ✅ EKLENDİ
                    activityLevel = args.activityLevel // ✅ EKLENDİ
                )
                findNavController().navigate(action)
                viewModel.navigationComplete()
            }
        }
    }

    private fun updateUnitUI(
        selectedView: TextView, unselectedView: TextView, currentUnit: String, selectedTag: String, unselectedTag: String
    ) {
        val context = requireContext()
        val primaryColor = ContextCompat.getColor(context, R.color.black)
        val secondaryColor = ContextCompat.getColor(context, R.color.white)

        if (currentUnit == selectedTag) {
            selectedView.setBackgroundResource(R.drawable.unit_selector_selected)
            selectedView.setTextColor(secondaryColor)
            unselectedView.setBackgroundResource(R.drawable.unit_selector_unselected)
            unselectedView.setTextColor(primaryColor)
        } else {
            unselectedView.setBackgroundResource(R.drawable.unit_selector_selected)
            unselectedView.setTextColor(secondaryColor)
            selectedView.setBackgroundResource(R.drawable.unit_selector_unselected)
            selectedView.setTextColor(primaryColor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}