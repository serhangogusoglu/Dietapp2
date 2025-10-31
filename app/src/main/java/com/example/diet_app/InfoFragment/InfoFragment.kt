package com.example.diet_app.InfoFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.diet_app.InfoFragment.BottomSheet.PurposeSelectionBottomSheet
import com.example.diet_app.InfoFragment.BottomSheet.PurposeSelectionListener
import com.example.diet_app.R
import com.example.diet_app.databinding.FragmentInfoBinding

class InfoFragment : Fragment(), PurposeSelectionListener {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    // ViewModel'i oluştururken varsayılan factory kullanılır
    private val viewModel: InfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ülke kodu, bayraksız simülasyon olarak korunmuştur.
        setupCountryCodeDropdown()
        setupInputBinding()
        setupListeners()
        setupObservers()
    }

    private fun setupCountryCodeDropdown() {
        // Simülasyon: Bayraksız ülke kodları listesi
        val countryCodes = listOf("+46", "+90", "+1", "+44", "+49", "+33", "+81", "+86")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            countryCodes
        )

        // Bu yapı, AutoCompleteTextView'i bulmak için önceki XML yapınızla uyumludur
        val countryCodeInput = binding.layoutPhoneNumber.findViewById<AutoCompleteTextView>(R.id.input_country_code)
        countryCodeInput.setAdapter(adapter)

        countryCodeInput.setOnClickListener {
            countryCodeInput.showDropDown()
        }

        countryCodeInput.setOnItemClickListener { parent, view, position, id ->
            val selectedCode = parent.getItemAtPosition(position) as String
            // Seçilen kodu ViewModel'e kaydetme mantığı buraya gelir
            // (Opsiyonel olarak ViewModel'de bir LiveData tanımlanmalıdır)
        }
    }

    private fun setupInputBinding() {
        fun bindTextWatcher(editText: EditText, liveData: MutableLiveData<String>) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    liveData.value = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            })
        }

        bindTextWatcher(binding.inputName, viewModel.name)
        bindTextWatcher(binding.inputNickname, viewModel.nickname)
        bindTextWatcher(binding.inputEmail, viewModel.email)
        bindTextWatcher(binding.inputPhone, viewModel.phoneNumber)
        bindTextWatcher(binding.inputCountry , viewModel.country)

        // ❌ KALDIRILDI: Bu simülasyon kaldırıldı, artık BottomSheet açıldığında değer atanacak
        // binding.inputPurpose.setOnClickListener { ... }
    }

    private fun setupListeners() {
        binding.buttonNext.setOnClickListener {
            viewModel.onNextClicked()
        }

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // 'You're here for*' alanına tıklama olayını koruduk
        binding.inputPurpose.setOnClickListener {
            showPurposeSelectionBottomSheet()
        }
    }

    private fun showPurposeSelectionBottomSheet() {
        val bottomSheet = PurposeSelectionBottomSheet()
        bottomSheet.purposeSelectionListener = this
        bottomSheet.show(parentFragmentManager, "PurposeSelectionTag")
    }

    // PurposeSelectionListener arayüzünden gelen geri bildirim
    override fun onPurposeSelected(purpose: String) {
        // ✅ ViewModel'de amaç güncellenir. Bu, isNextButtonEnabled'i tetikler.
        binding.inputPurpose.text = purpose
        viewModel.purpose.value = purpose

        // Seçim yapıldıktan sonra TextView'ın rengini ayarla
        binding.inputPurpose.setTextColor(resources.getColor(android.R.color.black))
    }

    private fun setupObservers() {
        // Hata mesajlarını gözlemle
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                binding.textError.text = message
                binding.textError.visibility = View.VISIBLE
            } else {
                binding.textError.visibility = View.GONE
            }
        }

        // ✅ ZORUNLULUK KONTROLÜ: Next butonunun etkinliğini gözlemle
        viewModel.isNextButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.buttonNext.isEnabled = isEnabled
            // İsteğe bağlı: Butonun rengini de değiştirebilirsiniz (Örn: alpha veya backgroundTint)
        }

        // Sonraki adıma geçişi gözlemle
        viewModel.navigateToNextStep.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                // InfoStepTwoFragment'a geçiş aksiyonu
                findNavController().navigate(R.id.action_infoFragment_to_infoSecondFragment)
                viewModel.navigationComplete()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}