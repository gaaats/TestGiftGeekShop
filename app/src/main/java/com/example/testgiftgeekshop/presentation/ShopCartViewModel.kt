package com.example.testgiftgeekshop.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.utils.ShopCartState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ShopCartViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private var _listShopCart = MutableLiveData<MutableList<GeekProductUI>>()
    val listShopCart: LiveData<MutableList<GeekProductUI>>
        get() = _listShopCart

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

            elementAlreadyThere.countInShopList++
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


    fun minusProductFromShopCart(geekProductUI: GeekProductUI){
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
    }


    fun removeFromShopCart(geekProductUI: GeekProductUI) {
        val prePrice = _totalSum.value ?: 0
        val temp = mutableListOf<GeekProductUI>()
        geekProductUI.isElementInShopList = false
        temp.addAll(_listShopCart.value!!)
        val index = temp.indexOfFirst {
            it.id == geekProductUI.id
        }

        temp.removeAt(index)
        _listShopCart.value = temp
        _totalSum.value = prePrice - geekProductUI.price
    }

    private fun checkIsShopCartIsEmpty(){
        if (_totalSum.value ==0){
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
    fun getTotalSum() {
        var sum = 0
        _listShopCart.value!!.forEach {
            sum += it.sumInShopList
        }
        _totalSum.value = sum
    }
}