package com.example.diet_app.InfoSecondFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diet_app.R
import com.example.diet_app.databinding.BottomSheetAvatarSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


interface AvatarSelectionListener {
    fun onAvatarSelected(avatarResId: Int)
}

class AvatarSelectionBottomSheet: BottomSheetDialogFragment(), AvatarAdapter.OnAvatarClickListener {

    private var _binding: BottomSheetAvatarSelectionBinding? = null
    private val binding get() = _binding!!

    var selectionListener: AvatarSelectionListener?= null

    private val avatarResources = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.man,
        R.drawable.woman,
        R.drawable.gamer,
        // AVATAR RESİMLERİ EKLE
    )

    private var selectedAvatarResId: Int = 0// varsayılan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAvatarSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (selectedAvatarResId != 0) {
            binding.imageSelectedAvatarPreview.setImageResource(selectedAvatarResId)
        } else {
            binding.imageSelectedAvatarPreview.setImageResource(R.drawable.ic_launcher_background)
        }

        val adapter = AvatarAdapter(avatarResources, this)
        binding.recyclerAvatars.apply {
            layoutManager = GridLayoutManager(context, 3) // 3 sütunlu grid
            this.adapter = adapter
        }

        binding.buttonCancel.setOnClickListener { dismiss() }

        binding.buttonOk.setOnClickListener {
            if (selectedAvatarResId != 0) {
                Log.d("AvatarSheet", "Avatar seçildi: $selectedAvatarResId")
                selectionListener?.onAvatarSelected(selectedAvatarResId)
            } else {
                Log.d("AvatarSheet", "Avatar seçilmedi!")
            }
            dismiss()
        }


        // binding.imageSelectedAvatarPreview.setImageResource(selectedAvatarResId)
    }

    override fun onAvatarClick(avatarResId: Int) {
        selectedAvatarResId = avatarResId
        binding.imageSelectedAvatarPreview.setImageResource(avatarResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundResource(android.R.color.transparent)
    }

}