package com.example.diet_app.InfoFragment.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.diet_app.R
import com.example.diet_app.databinding.BottomSheetPurposeSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface PurposeSelectionListener {
    fun onPurposeSelected(purpose: String)
}

class PurposeSelectionBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetPurposeSelectionBinding? = null
    private val binding get() = _binding!!

    // Listener'ı Fragment'tan alacak
    var purposeSelectionListener: PurposeSelectionListener? = null

    // Amaç listesi
    private val purposes = listOf("Loosing Weight", "Gaining Weight", "Keeping Weight", "Being Weight")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Layout dosyanızın adı 'bottom_sheet_purpose_selection.xml' olmalıdır.
        _binding = BottomSheetPurposeSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // purpose_container, XML dosyasındaki LinearLayout ID'si olmalıdır.
        val container = binding.purposeContainer

        purposes.forEach { purpose ->
            val textView = TextView(context).apply {
                text = purpose
                textSize = 18f
                // Düzgün bir görünüm için padding
                setPadding(30, 40, 30, 40)
                setOnClickListener {
                    purposeSelectionListener?.onPurposeSelected(purpose)
                    dismiss() // Seçim yapıldıktan sonra BottomSheet'i kapat
                }
            }
            container.addView(textView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}