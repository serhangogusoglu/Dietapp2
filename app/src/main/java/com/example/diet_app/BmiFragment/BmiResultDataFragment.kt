package com.example.diet_app.BmiFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.diet_app.databinding.FragmentBmiResultDataBinding

class BmiResultDataFragment : Fragment() {

    private var _binding: FragmentBmiResultDataBinding? = null
    private val binding get() = _binding!!

    // Safe Args ile gelen verileri al
    private val args: BmiResultDataFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBmiResultDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textYourHeight.text = args.height + " " + "cm"
        binding.textYourWeight.text = args.weight + " " + "kg"

        // Verileri ekrana yaz
        binding.textYourBmi.text = args.bmiValue.toString()
        binding.labelOverweight.text = args.category

        // Devam butonuna tıklanınca ana sayfaya yönlendir
      //  binding.buttonYourTarget.setOnClickListener {
      //      // NOT: Navigation Graph'ta action_bmiResultFragment_to_homeFragment tanımlı
      //      val action = BmiResultDataFragmentDirections.actionBmiResultFragmentToHomeFragment()
      //      findNavController().navigate(action)
      //  }

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonYourTarget.setOnClickListener {
            val action = BmiResultDataFragmentDirections.actionBmiResultFragmentToTargetFragment(
                height = args.height,
                weight = args.weight,
                age = args.age,
                gender = args.gender,
                activityLevel = args.activityLevel
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}