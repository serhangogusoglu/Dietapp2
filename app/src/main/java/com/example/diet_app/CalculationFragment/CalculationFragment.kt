package com.example.diet_app.CalculationFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // Paylaşılan ViewModel için
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.diet_app.databinding.FragmentCalculationBinding
import com.example.diet_app.ui.info_second.InfoSecondViewModel
import com.example.diet_app.ui.info_second.TargetFragment

class CalculationFragment : Fragment() {

    private var _binding: FragmentCalculationBinding? = null
    private val binding get() = _binding!!

    // activityViewModels ile ViewModel'i paylaş
    private val viewModel: InfoSecondViewModel by activityViewModels()

    private val args: CalculationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel'den gelen navigasyon olayını dinle
        viewModel.navigateToBmiResult.observe(viewLifecycleOwner) { resultData ->
            resultData?.let {
                if (it.targetFragment == TargetFragment.BMI_RESULT) {

                    // BMI Sonuç Fragment'a yönlendir (Safe Args kullanarak)
                    val action = CalculationFragmentDirections.actionCalculationFragmentToBmiResultFragment(
                        bmiValue = it.bmi.toFloat(),
                        category = it.category,
                        height = args.height,
                        weight = args.weight,
                        age = args.age,
                        gender = args.gender,
                        activityLevel = args.activityLevel
                    )
                    findNavController().navigate(action)

                    // Navigasyonu tamamla (ViewModel'deki LiveData'yı temizle)
                    viewModel.navigationToBmiResultComplete()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}