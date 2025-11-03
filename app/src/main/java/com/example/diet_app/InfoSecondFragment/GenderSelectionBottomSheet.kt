package com.example.diet_app.InfoSecondFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diet_app.databinding.BottomSheetGenderSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface GenderSelectionListener {
    fun onGenderSelected(gender: String)
}

class GenderSelectionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetGenderSelectionBinding? = null
    private val binding get() = _binding!!

    var selectionListener: GenderSelectionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetGenderSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGenderMale.setOnClickListener {
            selectionListener?.onGenderSelected("Male")
            dismiss()
        }

        binding.buttonGenderFemale.setOnClickListener {
            selectionListener?.onGenderSelected("Female")
            dismiss()
        }

        binding.textCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}