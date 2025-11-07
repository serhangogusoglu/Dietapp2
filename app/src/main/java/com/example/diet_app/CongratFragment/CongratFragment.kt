package com.example.diet_app.CongratFragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentCongratBinding

class CongratFragment : Fragment() {

    private var _binding: FragmentCongratBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CongratViewModel by viewModels()

    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        viewModel.onCompleteClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCongratBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        handler.postDelayed(navigateRunnable, 3000)
    }

    private fun setupObservers() {
        viewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if(navigate) {
                val action = CongratFragmentDirections.actionCongratFragmentToHomeFragment()
                findNavController().navigate(action)

                viewModel.navigationComplete()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(navigateRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}