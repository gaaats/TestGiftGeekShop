package com.geekshop.testgiftgeekshop

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.geekshop.testgiftgeekshop.R.*
import com.geekshop.testgiftgeekshop.databinding.FragmentCompleteOrderBinding
import com.geekshop.testgiftgeekshop.presentation.ShopCartViewModel
import com.geekshop.testgiftgeekshop.utils.OrderStatusVrapper
import com.google.android.material.R
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
@FragmentScoped
class CompleteOrderFragment : Fragment() {

    private var deliveryCompanyName: String = ""
    private var region: String = ""
    private var paymentType: String = ""
    private var _binding: FragmentCompleteOrderBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")
    private val shopCartViewModel by activityViewModels<ShopCartViewModel>()
    private val novaPoshtaName by lazy {
        requireContext().getString(string.nova_poshta_name)
    }
    private val ukrPoshtaName by lazy {
        requireContext().getString(string.ukrposhta_name)
    }

    private val novaPoshtaPaymentVar by lazy {
        requireContext().resources.getStringArray(array.nova_poshta_payments_var)
    }

    private val ukrposhtaPaymentVar by lazy {
        requireContext().resources.getStringArray(array.ukrposhta_payments_var)
    }

    private val samovivozPaymentVar by lazy {
        requireContext().resources.getStringArray(array.samovivoz_payments_var)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initMainFunObserveCollectFlowFragment()
        addTextChangedListenerWatcher(binding.textInputFirstName)
        addTextChangedListenerWatcher(binding.textInputSecondName)
        addTextChangedListenerWatcher(binding.textInputPhoneNumber)
        addTextChangedListenerWatcher(binding.textInputDeliveryDepNumber)
        addTextChangedListenerWatcher(binding.textInputCity)

        initBtnConfirmDeliveryAndTotalSetOnClickListener()
        initAdapterSpinnerDeliveryCompany()
        initSpinnerDeliveryCompOnItemSelectedListener()
        initSpinnerRegionsOnItemSelectedListener()

        initSpinnerPaymentTypeOnItemSelectedListener()
        lottieAnimQuestionSamovivozSetOnClickListener()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun lottieAnimQuestionSamovivozSetOnClickListener() {
        binding.lottieAnimQuestionSamovivoz.setOnClickListener {
            initAlertDialog()
        }
    }

    private fun initAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Самовивіз")
            .setMessage("По Києву є самовивіз за адресою вул. Тампере 17/2, Лівий берег. \n" +
                    "При спілкуванні з менеджером просто скажіть, що бажаєте самостійно забрати замовлення.\n" +
                    "Дата та час самовивозу мають бути попередньо узгоджені.\n" +
                    "Решту деталей Вам повідомить менеджер.\n" +
                    "❤")
            .setPositiveButton("Зрозуміло") { _, _ ->
            }
            .setCancelable(true)
            .create()
            .show()

    }

    private fun initBtnConfirmDeliveryAndTotalSetOnClickListener() {
        binding.btnConfirmDeliveryAndTotal.setOnClickListener {
            acceptOrder()
//            navigateToThankYouFragment()
        }
    }

    private fun navigateToThankYouFragment() {
        findNavController().navigate(com.geekshop.testgiftgeekshop.R.id.action_completeOrderFragment_to_thankYouFragment)
    }

    private fun initMainFunObserveCollectFlowFragment() {
        collectFlowFragment(shopCartViewModel.orderStatusInput) {
            when (it) {
                is OrderStatusVrapper.Success -> {

                    Log.d("state_test", "state  is Success")
                    Log.d("state_test", shopCartViewModel.finalConfirmOrder())
                    shopCartViewModel.finalConfirmOrder()
                    navigateToThankYouFragment()
                }
                is OrderStatusVrapper.Error -> {
                    Log.d("state_test", "state  is Error")
                    if (it.safeError.errorFirstName) {
                        binding.textInLayFirstName.error = "помилка, перевірте дані"
                    }
                    if (it.safeError.errorSecondName) {
                        binding.textInLaySecondName.error = "помилка, перевірте дані"
                    }
                    if (it.safeError.errorPhoneNumber) {
                        binding.textInLayPhoneNumber.error = "помилка, перевірте дані"
                    }
                    if (it.safeError.errorDeliveryDepartmentNumber) {
                        binding.textInLayDeliveryDepNumber.error = "помилка, перевірте дані"
                    }
                    if (it.safeError.errorCity) {
                        binding.textInLayCity.error = "помилка, перевірте дані"
                    }
                }
                is OrderStatusVrapper.InProcess -> {
                    Log.d("state_test", "state  is InProcess")
                    binding.textInLayFirstName.error = null
                    binding.textInLaySecondName.error = null
                    binding.textInLayCity.error = null
                    binding.textInLayPhoneNumber.error = null
                    binding.textInLayDeliveryDepNumber.error = null
                }
            }
        }
    }


    private fun initAdapterSpinnerDeliveryCompany() {
        val array = arrayOf(novaPoshtaName, ukrPoshtaName)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            array
        )
        binding.spinnerDeliveryCompany.adapter = adapter
    }

    private fun initAdapterSpinnerPaymentType(array: Array<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            array
        )
        binding.spinnerPaymentVar.adapter = adapter
    }

    private fun initSpinnerPaymentTypeOnItemSelectedListener() {
        binding.spinnerPaymentVar.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    viev: View?,
                    position: Int,
                    id: Long
                ) {
                    paymentType = adapter?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun initSpinnerDeliveryCompOnItemSelectedListener() {
        binding.spinnerDeliveryCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    viev: View?,
                    position: Int,
                    id: Long
                ) {
                    deliveryCompanyName = adapter?.getItemAtPosition(position).toString()
                    when (deliveryCompanyName) {
                        novaPoshtaName -> {
                            initAdapterSpinnerPaymentType(novaPoshtaPaymentVar)
                        }
                        ukrPoshtaName -> {
                            initAdapterSpinnerPaymentType(ukrposhtaPaymentVar)
                        }
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun initSpinnerRegionsOnItemSelectedListener() {
        binding.spinnerUkraineRegions.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    viev: View?,
                    position: Int,
                    id: Long
                ) {
                    region = adapter?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun acceptOrder() {
        val firstName: String = binding.textInputFirstName.text.toString()
        val secondName: String = binding.textInputSecondName.text.toString()
        val phoneNumber: String = binding.textInputPhoneNumber.text.toString()
        val city: String = binding.textInputCity.text.toString()
        val deliveryDepartmentNumber: String = binding.textInputDeliveryDepNumber.text.toString()
        shopCartViewModel.acceptOrder(
            deliveryCompanyName = deliveryCompanyName,
            firstName = firstName,
            secondName = secondName,
            phoneNumber = phoneNumber,
            region = region,
            city = city,
            deliveryDepartmentNumber = deliveryDepartmentNumber,
            paymentType = paymentType,
        )
    }

    private fun addTextChangedListenerWatcher(
        textInputEditText: TextInputEditText
    ) {
        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //here
                shopCartViewModel.cleanAllErrors()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

fun <T> Fragment.collectFlowFragment(
    flow: Flow<T>,
    functionSuspend: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(functionSuspend)
        }
    }
}