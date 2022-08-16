package com.example.testgiftgeekshop.presentation

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.R
import com.example.testgiftgeekshop.data.entity.ErrorInputModel
import com.example.testgiftgeekshop.domain.entity.*
import com.example.testgiftgeekshop.utils.OrderStatusVrapper
import com.example.testgiftgeekshop.utils.ResourceVrap
import com.example.testgiftgeekshop.utils.ShopCartState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShopCartViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

//    private val _orderEntity = MutableStateFlow(OrderEntity())
//    val orderEntity: StateFlow<OrderEntity> = _orderEntity

    private val _orderStatusInput = MutableStateFlow<OrderStatusVrapper>(OrderStatusVrapper.InProcess)
    val orderStatusInput: StateFlow<OrderStatusVrapper> = _orderStatusInput

    private val _errorInputEntity = MutableStateFlow(ErrorInputModel())
    val errorInputEntity: StateFlow<ErrorInputModel> = _errorInputEntity

    val novaPoshtaName by lazy {
        application.getString(com.example.testgiftgeekshop.R.string.nova_poshta_name)
    }
    val ukrPoshtaName by lazy {
        application.getString(com.example.testgiftgeekshop.R.string.ukrposhta_name)
    }

    private var _errorModel = MutableLiveData<ErrorInputModel>()
    val errorModel: LiveData<ErrorInputModel>
        get() = _errorModel

    private var _listShopCart = MutableLiveData<MutableList<GeekProductUI>>()
    val listShopCart: LiveData<MutableList<GeekProductUI>>
        get() = _listShopCart

    private var _userTakeDeliveryCompany = MutableLiveData<DeliveryCompany>()
    val userTakeDeliveryCompany: LiveData<DeliveryCompany>
        get() = _userTakeDeliveryCompany

    private var _totalSum = MutableLiveData<Int>()
    val totalSum: LiveData<Int>
        get() = _totalSum

    private var _isShopCartEmpty = MutableLiveData<Boolean>()
    val isShopCartEmpty: LiveData<Boolean>
        get() = _isShopCartEmpty


    init {
        _isShopCartEmpty.value = true
        _totalSum.value = 0
        _listShopCart.value = mutableListOf()
        _errorModel.value = ErrorInputModel()
//        _userTakeDeliveryCompany.value = NovaPoshta(1)
    }

    fun addToShopCart(geekProductUI: GeekProductUI) {
        val prePrice = _totalSum.value ?: 0
        if (checkIsItemAlreadyInShopCart(geekProductUI)) {
            val elementAlreadyThere = _listShopCart.value!!.find {
                it.id == geekProductUI.id
            }!!.copy()
            val sumInShopList = elementAlreadyThere.sumInShopList
            elementAlreadyThere.countInShopList++

            elementAlreadyThere.sumInShopList = sumInShopList + elementAlreadyThere.price
            val index = _listShopCart.value!!.indexOfFirst {
                it.id == elementAlreadyThere.id
            }

            _listShopCart.value!![index] = elementAlreadyThere
            _totalSum.value = prePrice + geekProductUI.price

            // do not need
            // refreshShopCart()

            return
        }
        val element = geekProductUI.copy()
        _listShopCart.value!!.add(element)
        _totalSum.value = prePrice + geekProductUI.price

        makeShopCartStateIsFull()
        //
        // do not need
        //refreshShopCart()
    }

    private fun makeShopCartStateIsFull() {
        _isShopCartEmpty.value = false
    }


    fun minusProductFromShopCart(geekProductUI: GeekProductUI) {
        val prePrice = _totalSum.value ?: 0
        val elementAlreadyThere = _listShopCart.value!!.find {
            it.id == geekProductUI.id
        }!!.copy()

        elementAlreadyThere.countInShopList--

        val index = _listShopCart.value!!.indexOfFirst {
            it.id == elementAlreadyThere.id
        }
        _listShopCart.value!![index] = elementAlreadyThere
        _totalSum.value = prePrice - geekProductUI.price

        checkIsShopCartIsEmpty()
    }


    fun removeFromShopCart(geekProductUI: GeekProductUI, countProductPreDelete: Int) {
        val prePrice = _totalSum.value ?: 0
        val temp = mutableListOf<GeekProductUI>()
        geekProductUI.isElementInShopList = false
        temp.addAll(_listShopCart.value!!)
        val index = temp.indexOfFirst {
            it.id == geekProductUI.id
        }

        temp.removeAt(index)
        _listShopCart.value = temp
        _totalSum.value = prePrice - (geekProductUI.price * countProductPreDelete)

        checkIsShopCartIsEmpty()
    }

    private fun checkIsShopCartIsEmpty() {
        if (_totalSum.value == 0) {
            _isShopCartEmpty.value = true
        }
    }

    fun refreshShopCart() {
        val temp = mutableListOf<GeekProductUI>()
        temp.addAll(_listShopCart.value!!)
        _listShopCart.value = temp
    }

    private fun checkIsItemAlreadyInShopCart(geekProductUI: GeekProductUI): Boolean {
        _listShopCart.value!!.forEach {
            if (it.id == geekProductUI.id) return true
        }
        return false
    }

    //test
    fun refreshTotalSumShopCart() {
        var sum = 0
        _listShopCart.value!!.forEach {
            sum += it.sumInShopList
        }
        _totalSum.value = sum
        checkIsShopCartIsEmpty()
    }

    fun sendOrderToNusick(): Intent {

        val text = "тут буде текст замовлення"
        val number = "+380938858600"
        val applicationpackage = "com.whatsapp"

        if (checkIsWattsUppInstalled()) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=${number}&text=${text}")
            )
            return intent
        }
        throw RuntimeException("Встановіть, будь ласка, WattsUpp")
    }

    private fun checkIsWattsUppInstalled(): Boolean {
        val applicationpackage = "com.whatsapp"
        val packageManager = application.packageManager
        return try {
            packageManager.getPackageInfo(applicationpackage, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun createNevOrder(
        name: String,
        secondName: String,
        deliveryType: DeliveryModel,
        totalSum: Int,
        phoneNumberContact: String
    ): OrderEntity {
        return OrderEntity(
            name, secondName, deliveryType, totalSum, phoneNumberContact
        )
    }

//    fun createDeliveryModel(
//        region: String,
//        city: String,
//        type: DeliveryCompany,
//        deliveryPhoneNumber: String
//    ): DeliveryModel {
////        return DeliveryModel(region, city, type, deliveryPhoneNumber)
//    }

    fun chooseDeliveryCompany(
        userTakeDeliveryCompany: String,
        deliveryDepartmentNumber: String
    ): DeliveryCompany {
        return when (userTakeDeliveryCompany) {
            novaPoshtaName -> NovaPoshta(deliveryDepartmentNumber)
            ukrPoshtaName -> UkrPoshta(deliveryDepartmentNumber)
            else -> {
                throw RuntimeException("there is no such delivery company")
            }
        }
    }

//    private fun parseNumber(inputNumber: String?): Int {
//        return try {
//            inputNumber?.trim()?.toInt() ?: 0
//        } catch (e: Exception) {
//            0
//        }
//    }

    private fun parseString(inputString: String?): String {
        return inputString?.trim() ?: ""
    }

    private fun validating(name: String): Boolean {
        var result = true
        if (name.isBlank()) {
            result = false
//            _errorInputNameLD.value = true

        }
//        if (count <= 0) {
//            result = false
//            _errorInputNumberLD.value = true
//        }
        return result
    }

    fun acceptOrder(
        deliveryCompanyName: String,
        firstName: String,
        secondName: String,
        phoneNumber: String,
        region: String,
        city: String,
        deliveryDepartmentNumber: String,
        paymentType: String,
    ) = viewModelScope.launch {
        // checking
        val firstNameAfterParse = parseString(firstName)
        val secondNameAfterParse = parseString(secondName)
        val cityAfterParse = parseString(city)
        val phoneNumberAfterParse = parseString(phoneNumber)
        val deliveryDepartmentNumberAfterParse = parseString(deliveryDepartmentNumber)

        _orderStatusInput.value.error?.cleanAllErrors()

        if (!validating(firstNameAfterParse)){
            Log.d(
                "test_complete_frag",
                "i am in error validating(firstNameAfterParse)"
            )

            _errorInputEntity.value.errorFirstName = true
        }
        if (!validating(secondNameAfterParse)){
            _errorInputEntity.value.errorSecondName = true
        }
        if (!validating(phoneNumberAfterParse)){
            _errorInputEntity.value.errorPhoneNumber = true
        }
        if (!validating(cityAfterParse)){
            _errorInputEntity.value.errorCity = true
        }
        if (!validating(deliveryDepartmentNumberAfterParse)){
            _errorInputEntity.value.errorDeliveryDepartmentNumber = true
        }
        Log.d(
            "test_complete_frag",
            "errorFirstName ${_errorInputEntity.value.errorFirstName}"
        )
        Log.d(
            "test_complete_frag",
            "isThereAnyError ${_errorInputEntity.value.isThereAnyError()}"
        )

        // MAIN CHECK FOR ERRORS inside INPUT TEXT
        if (_errorInputEntity.value.isThereAnyError()){
            _orderStatusInput.value = OrderStatusVrapper.Error(_errorInputEntity.value)
        } else{
            val deliveryCompany = chooseDeliveryCompany(
                deliveryCompanyName, deliveryDepartmentNumber
            )
            val successOrderEntity = OrderEntity(
                name = firstName,
                secondName = secondName,
                deliveryType = DeliveryModel(
                    region = region,
                    city = city,
                    deliveryCompany = deliveryCompany,
                    deliveryPhoneNumber = phoneNumber
                ),
                totalSum = _totalSum.value!!,
                phoneNumberContact = phoneNumber,
                paymentType = paymentType
            )
            _orderStatusInput.value = OrderStatusVrapper.Success(successOrderEntity)
        }
    }

    fun cleanAllErrors() = viewModelScope.launch {
        _orderStatusInput.value.error?.cleanAllErrors()
        _orderStatusInput.value = OrderStatusVrapper.InProcess
    }
}