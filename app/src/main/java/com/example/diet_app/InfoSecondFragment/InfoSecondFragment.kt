package com.example.diet_app.info_second

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.diet_app.InfoSecondFragment.AvatarSelectionBottomSheet
import com.example.diet_app.InfoSecondFragment.AvatarSelectionListener
import com.example.diet_app.InfoSecondFragment.PictureOption
import com.example.diet_app.InfoSecondFragment.ProfilePictureSelectionBottomSheet
import com.example.diet_app.InfoSecondFragment.ProfilePictureSelectionListener
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentInfoSecondBinding
import com.example.diet_app.ui.info_second.InfoSecondViewModel

class InfoSecondFragment : Fragment(), ProfilePictureSelectionListener, AvatarSelectionListener {

    private var _binding: FragmentInfoSecondBinding? = null
    private val binding get() = _binding!!

    // ViewModel'i doğru paketten çağır
    private val viewModel: InfoSecondViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUnitSelectors()
        setupInputBinding()
        setupListeners()
        setupObservers()
    }

    // MARK: - Avatar Seçim Geri Bildirimleri (Listeners)

    // ✅ AvatarSelectionBottomSheet'ten gelen geri bildirim (Kullanıcının seçimi)
    override fun onAvatarSelected(avatarResId: Int) {
        Log.d("InfoSecondFragment", "onAvatarSelected çağrıldı: $avatarResId")
        viewModel.profileImageResId.value = avatarResId
        binding.imageProfile.setImageResource(avatarResId)
    }

    // ✅ ProfilePictureSelectionBottomSheet'ten gelen geri bildirim
    override fun onPictureOptionSelected(option: PictureOption) {
        when(option) {
            PictureOption.CHOOSE_AVATAR -> {
                showAvatarSelectionSheet()
            }
            PictureOption.CHOOSE_FROM_LIBRARY -> {
                Toast.makeText(context, "Galeriden Seç", Toast.LENGTH_SHORT).show()
            }
            PictureOption.TAKE_PHOTO -> {
                Toast.makeText(context, "Fotoğraf Çek", Toast.LENGTH_SHORT).show()
            }
            PictureOption.REMOVE_CURRENT_PICTURE -> {
                // Varsayılan resme dön: ViewModel'i ve View'i güncelle
                val defaultResId = R.drawable.logo // Veya kullandığınız default logo
                viewModel.profileImageResId.value = defaultResId
                binding.imageProfile.setImageResource(defaultResId)

                Toast.makeText(context, "Resim Kaldırıldı", Toast.LENGTH_SHORT).show()
            }
            PictureOption.NONE -> { /* İşlem yok */ }
        }
    }


    // MARK: - Setup Metotları (Birleştirilmiş Metotlar)

    private fun setupUnitSelectors() {
        // Unit Observers and Listeners (Bir önceki versiyonla aynı)
        // ...

        viewModel.heightUnit.observe(viewLifecycleOwner) { unit ->
            updateUnitUI(binding.unitHeightCm, binding.unitHeightFt, unit, "cm", "ft")
        }
        viewModel.weightUnit.observe(viewLifecycleOwner) { unit ->
            updateUnitUI(binding.unitWeightKg, binding.unitWeightLbs, unit, "kg", "lbs")
        }

        binding.unitHeightFt.setOnClickListener { viewModel.onHeightUnitChanged("ft") }
        binding.unitHeightCm.setOnClickListener { viewModel.onHeightUnitChanged("cm") }
        binding.unitWeightLbs.setOnClickListener { viewModel.onWeightUnitChanged("lbs") }
        binding.unitWeightKg.setOnClickListener { viewModel.onWeightUnitChanged("kg") }
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

    private fun setupInputBinding() {
        // Text Watchers (Bir önceki versiyonla aynı)
        fun bindTextWatcher(editText: TextView, liveData: MutableLiveData<String>) { /* ... */ }

        bindTextWatcher(binding.inputHeight, viewModel.height)
        bindTextWatcher(binding.inputWeight, viewModel.weight)

        binding.inputWeekMovement.setOnClickListener { /* TODO */ }
        binding.inputGender.setOnClickListener { /* TODO */ }
        binding.inputBirthDate.setOnClickListener { /* TODO: DatePicker */ }
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.onNextClicked()
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Profil resmi tıklama alanı
        binding.imageProfile.setOnClickListener {
            showProfilePictureSelectionSheet()
        }
        // Eğer TextView de tıklanabilirse:
        // binding.root.findViewById<TextView>(R.id.text_add_profile_picture).setOnClickListener {
        //     showProfilePictureSelectionSheet()
        // }
    }

    private fun showProfilePictureSelectionSheet() {
        val bottomSheet = ProfilePictureSelectionBottomSheet()
        bottomSheet.selectionListener = this
        bottomSheet.show(parentFragmentManager, "ProfilePictureSelection")
    }

    private fun showAvatarSelectionSheet() {
        val avatarSheet = AvatarSelectionBottomSheet()
        avatarSheet.selectionListener = this
        avatarSheet.show(parentFragmentManager, "AvatarSelection")
    }


    // MARK: - LiveData Gözlemcileri (Observers)

    private fun setupObservers() {
        // Hata ve Buton gözlemcileri (Bir önceki versiyonla aynı)
        // ...

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                binding.textError.text = message
                binding.textError.visibility = View.VISIBLE
            } else {
                binding.textError.visibility = View.GONE
            }
        }

        viewModel.isNextButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.buttonNext.isEnabled = isEnabled
            binding.buttonNext.alpha = if (isEnabled) 1.0f else 0.5f
        }


        // ✅ PROFİL RESMİ GÖZLEMCİSİ (Fragment yeniden oluştuğunda resmi geri yükler)
        viewModel.profileImageResId.observe(viewLifecycleOwner) { resId ->
            if (resId != null && resId != 0) {
                // Sadece veriyi LiveData'dan geri yüklerken (e.g. ekran döndüğünde) View'i güncelle
                binding.imageProfile.setImageResource(resId)
            } else {
                // Resim kaldırıldıysa veya başlangıçta boşsa varsayılan ikonu göster
                binding.imageProfile.setImageResource(R.drawable.logo)
            }
        }

        // Ana sayfaya geçişi gözlemle
        viewModel.navigateToHome.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                // ...
                viewModel.navigationComplete()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}