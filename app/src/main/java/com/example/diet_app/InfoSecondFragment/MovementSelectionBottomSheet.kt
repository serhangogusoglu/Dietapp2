package com.example.diet_app.InfoSecondFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diet_app.databinding.BottomSheetMovementSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface MovementSelectionListener {
    fun onMovementSelected(movementLevel: String)
}

class MovementSelectionBottomSheet : BottomSheetDialogFragment(){

    private var _binding: BottomSheetMovementSelectionBinding? = null
    private val binding get() = _binding!!

    var selectionListener: MovementSelectionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetMovementSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickListener = View.OnClickListener { v ->
            val level = when(v.id) {
                binding.buttonSedentary.id -> "Sedentary"
                binding.buttonLightlyActive.id -> "Lightly Active"
                binding.buttonModeratelyActive.id -> "Moderately Active"
                binding.buttonVeryActive.id -> "Very Active"
                else -> return@OnClickListener
            }
            selectionListener?.onMovementSelected(level)
            dismiss()
        }

        binding.buttonSedentary.setOnClickListener(clickListener)
        binding.buttonLightlyActive.setOnClickListener(clickListener)
        binding.buttonModeratelyActive.setOnClickListener(clickListener)
        binding.buttonVeryActive.setOnClickListener(clickListener)

        binding.textCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}