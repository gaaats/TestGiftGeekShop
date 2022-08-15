package com.example.testgiftgeekshop.presentation

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.domain.entity.*
import com.example.testgiftgeekshop.utils.ShopCartState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ShopCartViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private var _listShopCart = MutableLiveData<MutableList<GeekProductUI>>()
    val listShopCart: LiveData<MutableList<GeekProductUI>>
        get() = _listShopCart

//    private var _userTakeDeliveryCompany = MutableLiveData<DeliveryCompany>()
//    val userTakeDeliveryCompany: LiveData<DeliveryCompany>
//        get() = _userTakeDeliveryCompany

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

    fun crateNevOrder(
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

    fun createDeliveryModel(
        region: String,
        city: String,
        type: DeliveryCompany,
        deliveryPhoneNumber: String
    ): DeliveryModel {
        return DeliveryModel(region, city, type, deliveryPhoneNumber)
    }

    fun chooseDeliveryCompany(
        userTakeDeliveryCompany: Int,
        deliveryDepartmentNumber: Int
    ): DeliveryCompany {
        return when (userTakeDeliveryCompany) {
            55 -> NovaPoshta(deliveryDepartmentNumber)
            66 -> return UkrPoshta(deliveryDepartmentNumber)
            else -> {
                throw RuntimeException("there is no such delivery company")
            }
        }
    }
}