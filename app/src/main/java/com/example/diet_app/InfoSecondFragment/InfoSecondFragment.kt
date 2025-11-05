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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.diet_app.DatePickerFragment
import com.example.diet_app.DateSelectionListener
import com.example.diet_app.InfoSecondFragment.AvatarSelectionBottomSheet
import com.example.diet_app.InfoSecondFragment.AvatarSelectionListener
import com.example.diet_app.InfoSecondFragment.GenderSelectionBottomSheet
import com.example.diet_app.InfoSecondFragment.GenderSelectionListener
import com.example.diet_app.InfoSecondFragment.MovementSelectionBottomSheet
import com.example.diet_app.InfoSecondFragment.MovementSelectionListener
import com.example.diet_app.InfoSecondFragment.PictureOption
import com.example.diet_app.InfoSecondFragment.ProfilePictureSelectionBottomSheet
import com.example.diet_app.InfoSecondFragment.ProfilePictureSelectionListener
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentInfoSecondBinding
import com.example.diet_app.ui.info_second.InfoSecondViewModel

class InfoSecondFragment : Fragment(), ProfilePictureSelectionListener, AvatarSelectionListener,
    DateSelectionListener, GenderSelectionListener, MovementSelectionListener {

    private var _binding: FragmentInfoSecondBinding? = null
    private val binding get() = _binding!!

    // ViewModel'i doğru paketten çağır
    private val viewModel: InfoSecondViewModel by activityViewModels()

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
        fun bindTextWatcher(editText: TextView, liveData: MutableLiveData<String>) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    liveData.value = s.toString()
                }
            })
        }

        bindTextWatcher(binding.inputHeight, viewModel.height)
        bindTextWatcher(binding.inputWeight, viewModel.weight)

        binding.inputWeekMovement.setOnClickListener {
            showMovementSelectionSheet()
        }
        binding.inputGender.setOnClickListener {
            showGenderSelectionSheet()
        }
        binding.inputBirthDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showMovementSelectionSheet(){
        val bottomSheet = MovementSelectionBottomSheet()
        bottomSheet.selectionListener = this
        bottomSheet.show(parentFragmentManager, "MovementSelection")
    }

    override fun onMovementSelected(movementLevel: String) {
        binding.inputWeekMovement.text = movementLevel

        viewModel.weekMovement.value = movementLevel
    }

    private fun showGenderSelectionSheet() {
        val bottomsheet = GenderSelectionBottomSheet()
        bottomsheet.selectionListener = this
        bottomsheet.show(parentFragmentManager, "GenderSelection")
    }

    override fun onGenderSelected(gender: String) {
        binding.inputGender.text = gender

        viewModel.gender.value = gender
    }

    private fun showDatePicker(){
        DatePickerFragment.show(this, this)
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
        binding.textAddProfilePicture.setOnClickListener {
            showProfilePictureSelectionSheet()
        }
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

        // ✅ YENİ: BMI Hesaplama ve Gecikmeli Navigasyon Gözlemcisi
        viewModel.navigateToBmiResult.observe(viewLifecycleOwner) { resultData ->
            resultData?.let {
                when (it.targetFragment) {
                    com.example.diet_app.ui.info_second.TargetFragment.CALCULATION -> {
                        // 1. Calculation Fragment'a yönlendir
                        // NOT: Navigasyon Grafiğinizde (nav_graph.xml)
                        // action_infoSecondFragment_to_calculationFragment tanımlı olmalıdır.

                        // Örnek Navigasyon kodu:
                         val action = InfoSecondFragmentDirections.actionInfoSecondFragmentToCalculationFragment(
                             bmiValue = it.bmi.toFloat(),
                             category = it.category,
                             height = viewModel.height.value ?: "",
                             weight = viewModel.weight.value ?: ""
                         )
                         findNavController().navigate(action)

                        // Şimdilik sadece Toast gösterelim ve butonu değiştirelim (Gerçek uygulamada CalculationFragment'a geçmelisiniz)
                        Toast.makeText(context, "BMI Hesaplaması Başlatıldı...", Toast.LENGTH_SHORT).show()

                        // Kullanıcıya bir geri bildirim vermek için butonu devre dışı bırakıp metnini değiştiriyoruz
                        binding.buttonNext.text = "Calculating..."
                        binding.buttonNext.isEnabled = false
                        binding.buttonNext.alpha = 1.0f
                    }
                    com.example.diet_app.ui.info_second.TargetFragment.BMI_RESULT -> {
                        // 2. BMI Sonuç Fragment'a yönlendir
                        // NOT: Navigasyon Grafiğinizde (nav_graph.xml) bu aksiyonun olması gerekir

                        // Örnek Navigasyon kodu (Doğrudan buradan):
                        // val action = InfoSecondFragmentDirections.actionInfoSecondFragmentToBmiResultFragment(
                        //     bmi = it.bmi.toFloat(),
                        //     category = it.category
                        // )
                        // findNavController().navigate(action)

                        // Şimdilik sadece Toast gösterelim
                        Toast.makeText(context, "Sonuç: BMI ${it.bmi} (${it.category})", Toast.LENGTH_LONG).show()

                        // Navigasyon sonrası temizlik ve buton geri dönüşü
                        viewModel.navigationToBmiResultComplete()
                        binding.buttonNext.text = "Calculate BMI and Weight"
                        binding.buttonNext.isEnabled = true
                        binding.buttonNext.alpha = 1.0f
                    }
                }
            }
        }


        // Ana sayfaya geçişi gözlemle (Sadece Skip butonu için geçerli olabilir)
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

    override fun onDateSelected(dateString: String) {
        binding.inputBirthDate.text = dateString

        // ViewModel'e kaydet (Form geçerliliği için gereklidir)
        viewModel.birthDate.value = dateString
    }
}