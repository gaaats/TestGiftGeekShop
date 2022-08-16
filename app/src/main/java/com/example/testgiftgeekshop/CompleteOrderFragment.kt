package com.example.testgiftgeekshop

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
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.testgiftgeekshop.databinding.FragmentCompleteOrderBinding
import com.example.testgiftgeekshop.presentation.ShopCartViewModel
import com.example.testgiftgeekshop.utils.OrderStatusVrapper
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
        requireContext().getString(com.example.testgiftgeekshop.R.string.nova_poshta_name)
    }
    private val ukrPoshtaName by lazy {
        requireContext().getString(com.example.testgiftgeekshop.R.string.ukrposhta_name)
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

        tnConfirmDeliveryAndTotalSetOnClickListener()
        initAdapterSpinnerDeliveryCompany()
        initSpinnerDeliveryCompOnItemSelectedListener()
        initSpinnerRegionsOnItemSelectedListener()

        initAdapterSpinnerPaymentType()
        initSpinnerPaymentTypeOnItemSelectedListener()


        super.onViewCreated(view, savedInstanceState)
    }

    private fun tnConfirmDeliveryAndTotalSetOnClickListener() {
        binding.btnConfirmDeliveryAndTotal.setOnClickListener {
            acceptOrder()
            //            findNavController().navigate(R.id.action_completeOrderFragment_to_thankYouFragment)
        }
    }

    private fun initMainFunObserveCollectFlowFragment() {
        collectFlowFragment(shopCartViewModel.orderStatusInput) {
            when (it) {
                is OrderStatusVrapper.Success -> {
                    Log.d("state_test", "state  is Success")
//                    Log.d(
//                        "state_test",
//                        "name is ${it.safeData.name}, payment is ${it.safeData.paymentType}"
//                    )
                    Log.d("state_test", shopCartViewModel.finalConfirmOrder())
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

    private fun initAdapterSpinnerPaymentType() {
        val array = arrayOf(
            "повна оплата на карту",
            "повна оплата на реєстраційний рахунок ФОП",
            "100 грн передплати, решта наложка",
            "оплата при отриманні",
        )
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