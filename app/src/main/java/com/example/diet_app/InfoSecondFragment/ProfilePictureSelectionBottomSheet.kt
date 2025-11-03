package com.example.diet_app.InfoSecondFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.example.diet_app.R
import com.example.diet_app.databinding.BottomSheetProfilePictureSelectionBinding // Binding sınıfınız
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface ProfilePictureSelectionListener {
    fun onPictureOptionSelected(option: PictureOption)
}

enum class PictureOption {
    CHOOSE_AVATAR,
    CHOOSE_FROM_LIBRARY,
    TAKE_PHOTO,
    REMOVE_CURRENT_PICTURE,
    NONE
}

class ProfilePictureSelectionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetProfilePictureSelectionBinding? = null
    private val binding get() = _binding!!

    var selectionListener: ProfilePictureSelectionListener? = null
    private var selectedOption: PictureOption = PictureOption.NONE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetProfilePictureSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroupProfilePicture.setOnCheckedChangeListener { group: RadioGroup,checkedId: Int ->
            selectedOption = when(checkedId) {
                R.id.radio_avatar -> PictureOption.CHOOSE_AVATAR
                R.id.radio_library -> PictureOption.CHOOSE_FROM_LIBRARY
                R.id.radio_take_photo -> PictureOption.TAKE_PHOTO
                R.id.radio_remove -> PictureOption.REMOVE_CURRENT_PICTURE
                else -> PictureOption.NONE
            }
        }

        binding.buttonOk.setOnClickListener {
            if(selectedOption != PictureOption.NONE) {
                selectionListener?.onPictureOptionSelected(selectedOption)
            } else {
                Toast.makeText(context, "Lütfen bir seçenek belirleyin", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}