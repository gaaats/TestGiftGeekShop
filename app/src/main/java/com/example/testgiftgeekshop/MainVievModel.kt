package com.example.testgiftgeekshop

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.domain.usecases.*
import com.example.testgiftgeekshop.utils.Constances
import com.example.testgiftgeekshop.utils.ModelAnswer
import com.example.testgiftgeekshop.utils.ResourceVrap
import com.example.testgiftgeekshop.utils.ScreenModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainVievModel @Inject constructor(
    private val application: Application,
    private val getProductInfo: GetProductInfo,
    private val getAllProductsByIDs: GetAllProductsByIDs,
    private val getAllProductsByGroupIds: GetAllProductsByGroupIds,
    private val sortByPrice: SortByPrice,
    private val filterResultListByTitle: FilterResultListByTitle,
) : ViewModel() {

    //todo: make it by flow later
    private var _listProducts = MutableLiveData<ResourceVrap<List<GeekProductUI>>>()
    val listProducts: LiveData<ResourceVrap<List<GeekProductUI>>>
        get() = _listProducts

    private var _screenFirstState = MutableLiveData<ScreenModel>()
    val screenFirstState: LiveData<ScreenModel>
        get() = _screenFirstState

    private var _screenSecondState = MutableLiveData<ScreenModel>()
    val screenSecondState: LiveData<ScreenModel>
        get() = _screenSecondState

    private var _screenThirdState = MutableLiveData<ScreenModel>()
    val screenThirdState: LiveData<ScreenModel>
        get() = _screenThirdState

    private var _screenFourthState = MutableLiveData<ScreenModel>()
    val screenFourthState: LiveData<ScreenModel>
        get() = _screenFourthState

    private var _singleProductOpened = MutableLiveData<ResourceVrap<GeekProductUI>>()
    val singleProductOpened: LiveData<ResourceVrap<GeekProductUI>>
        get() = _singleProductOpened

    private var _screenExternalMarvelState = MutableLiveData<ScreenModel>()
    val screenExternalMarvelState: LiveData<ScreenModel>
        get() = _screenExternalMarvelState

    private val mapKeysGroupId = mapOf(
        application.getString(R.string.question_1_for_parents) to 103894435,
        application.getString(R.string.question_2_squid_game) to 101743532
    )

    //id товаров валентинок  1560970884  1560994052  1561009067

    // папа и мама група 103894435


    // бокси гарри 98631385
    // шарфики апки перчатки 98631214
    //галстуки носочки 98631330
    // стикери наклейик 98631332
    // сладости гарри 98631374

    // палочка гарри 1444898799
    //1446238944
    //1446245094
    //1446247840
    //1446252142
    //1446255345


    //for future
//    private var _statusMessage  = MutableLiveData<SnackBarListener<String>>()
//    val statusMessage: LiveData<SnackBarListener<String>>
//        get() = _statusMessage
//
//    private val _navigateToNextScreen = MutableLiveData<Unit>()
//    val navigateToNextScreen: LiveData<Unit> = _navigateToNextScreen

    init {
        initFirstScreenState()
        initSecondScreenState()
        initThirdScreenState()
        initFourthScreenState()
        initExternalMarvelScreenState()
    }

    suspend fun loadPressedSingleProduct(productID: Int) {
        withContext(Dispatchers.IO) {
            _singleProductOpened.postValue(getProductInfo.execute(productID))
        }
        delay(1000)
        Log.d("element", " ${_singleProductOpened.value!!.data!!.name}")
    }

    //    fun checkParamsQuestion1(){
//        val list = screenFirstState.value!!.returnActiveParams()
//        val listParamsSearch = mutableListOf<Int>()
//
//        Log.d("test_params", "list is $list")
//        list.forEach { listParams ->
//            listParams.forEach { param ->
//                viewModelScope.launch (Dispatchers.IO) {
//                    _listProducts.postValue(getAllProductsByGroupId.execute(param))
//                }
//            }
//        }
//    }
    private fun checkParamsQuestion1() {
        val list1 = screenFirstState.value!!.returnActiveParams()
        val list2 = screenSecondState.value!!.returnActiveParams()
        val list3 = screenExternalMarvelState.value!!.returnActiveParams()
        val combinedListParams = mutableListOf<Int>()
        combinedListParams.addAll(list1)
        combinedListParams.addAll(list2)
        combinedListParams.addAll(list3)
        Log.d("test_params", "combined list is: $combinedListParams")
        combinedListParams.shuffle()

        //filters
        val filterListPrice = screenFourthState.value!!.returnActiveParams().sortedBy {
            it
        }
        Log.d("test_params", "max number is: ${filterListPrice.last()}")
        // title filter
        val filterTitleList = screenThirdState.value!!.returnActiveParams()
        Log.d("test_params", "title filter is: $filterTitleList")
        filterResultListByTitle.execute(filterTitleList)


        viewModelScope.launch(Dispatchers.IO) {
            _listProducts.postValue(getAllProductsByGroupIds.execute(combinedListParams))
            sortByPrice.execute(filterListPrice.last())
            filterResultListByTitle.execute(filterTitleList)
        }
    }

    private fun initThirdScreenState() {
        _screenThirdState.value = ScreenModel(
            ModelAnswer(
                application.getString(R.string.question_3_yes),
                false,
                listOf(Constances.ONLY_IN_GIFT_BOX)
            ),
            ModelAnswer(
                application.getString(R.string.question_3_no_thanks),
                false,
                listOf(Constances.ONLY_WITHOUT_GIFT_BOX)
            ),
            ModelAnswer(application.getString(R.string.question_3_both_variants), false),
        )
    }

    private fun initSecondScreenState() {
        _screenSecondState.value = ScreenModel(
            ModelAnswer(
                application.getString(R.string.question_2_harry_potter), false, listOf(
                    98631214,
                    98631438,
                    98631455,
                    98631390,
                    98631374,
                    98631385,
                    98631379,
                    98631460,
                    102343106
                )
            ),
            ModelAnswer(application.getString(R.string.question_2_marvel_dc), false),
            ModelAnswer(
                application.getString(R.string.question_2_squid_game),
                false,
                listOf(101743532)
            ),
            ModelAnswer(
                application.getString(R.string.question_2_game_thrones),
                false,
                listOf(101335494)
            ),
            ModelAnswer(
                application.getString(R.string.question_2_star_vars),
                false,
                listOf(102385802)
            ),
        )
    }

    private fun initExternalMarvelScreenState() {
        _screenExternalMarvelState.value = ScreenModel(
            ModelAnswer(
                application.getString(R.string.add_question_superman),
                false,
                listOf(99058288)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_batman),
                false,
                listOf(99056624)
            ),
            ModelAnswer(application.getString(R.string.add_question_loki), false, listOf(99055745)),
            ModelAnswer(
                application.getString(R.string.add_question_spider_man),
                false,
                listOf(99058288)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_iron_man),
                false,
                listOf(99041510)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_harley_quinn),
                false,
                listOf(99033919)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_deadpool),
                false,
                listOf(99988359)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_wolverine),
                false,
                listOf(104574408)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_winter_soldier),
                false,
                listOf(100339449)
            ),
            ModelAnswer(
                application.getString(R.string.add_question_marvel_all),
                false,
                listOf(99059554)
            ),
        )
    }

    private fun initFirstScreenState() {
        _screenFirstState.value = ScreenModel(
            ModelAnswer(application.getString(R.string.question_1_for_me), false),
            ModelAnswer(application.getString(R.string.question_1_for_friend), false),
            ModelAnswer(application.getString(R.string.question_1_for_love), false),
            ModelAnswer(
                application.getString(R.string.question_1_for_parents),
                false,
                listOf(103894435)
            ),
        )
    }

    private fun initFourthScreenState() {
        _screenFourthState.value = ScreenModel(
            ModelAnswer(application.getString(R.string.question_4_200), false, listOf(200)),
            ModelAnswer(application.getString(R.string.question_4_201_500), false, listOf(500)),
            ModelAnswer(application.getString(R.string.question_4_501_999), false, listOf(900)),
            ModelAnswer(application.getString(R.string.question_4_1000), false, listOf(5000)),
        )
    }

    fun loadList() {
        checkParamsQuestion1()
//        _listProducts.value = getAllProductsByGroupId.execute(98631438)
//        filterListByNumber()
    }

    private fun filterListByNumber(maxLimit: Int = 6) {
        var numberLimit = 0
        try {
            _listProducts.value = ResourceVrap.Success(_listProducts.value!!.data!!.filter {
                numberLimit++
                numberLimit <= maxLimit
            })
        } catch (e: Exception) {
            _listProducts.value = ResourceVrap.Error(exception = e)
        }
    }

    fun pressedAnswer1(textAnswer: String) {
        Log.d("string_test", "before is ${_screenFirstState.value}")
        _screenFirstState.value?.checkAnsAndChangeState(textAnswer)
        _screenFirstState.value!!.also {
            _screenFirstState.value = it
        }
        Log.d("string_test", "after is ${_screenFirstState.value}")
    }

    fun pressedAnswer2(textAnswer: String) {
        Log.d("string_test", "before is ${_screenSecondState.value}")
        _screenSecondState.value?.checkAnsAndChangeState(textAnswer)
        _screenSecondState.value!!.also {
            _screenSecondState.value = it
        }
        Log.d("string_test", "after is ${_screenSecondState.value}")
    }

    fun pressedAnswerExternalMarvel(textAnswer: String) {
        Log.d("string_test", "before is ${_screenExternalMarvelState.value}")
        _screenExternalMarvelState.value?.checkAnsAndChangeState(textAnswer)
        _screenExternalMarvelState.value!!.also {
            _screenExternalMarvelState.value = it
        }
        Log.d("string_test", "after is ${_screenExternalMarvelState.value}")
    }

    fun pressedAnswer3(textAnswer: String) {
        Log.d("string_test", "before is ${_screenThirdState.value}")
        _screenThirdState.value?.checkAnsAndChangeState(textAnswer)
        _screenThirdState.value!!.also {
            _screenThirdState.value = it
        }
        Log.d("string_test", "after is ${_screenThirdState.value}")
    }

    fun pressedAnswer4(textAnswer: String) {
        Log.d("string_test", "before is ${_screenFourthState.value}")
        _screenFourthState.value?.checkAnsAndChangeState(textAnswer)
        _screenFourthState.value!!.also {
            _screenFourthState.value = it
        }
        Log.d("string_test", "after is ${_screenFourthState.value}")
    }

    fun initCheckBoxCustom(imgView: ImageView, isActive: Boolean) {
        if (isActive) {
            imgView.setImageResource(R.drawable.icon_point_on)
            return
        }
        imgView.setImageResource(R.drawable.icon_point_off)
    }

    fun restartTest() {
        _listProducts = MutableLiveData<ResourceVrap<List<GeekProductUI>>>()
        initFirstScreenState()
        initSecondScreenState()
        initExternalMarvelScreenState()
        initThirdScreenState()
        initFourthScreenState()
    }

    suspend fun loadAllGoodsHarryPotter() {

    }

    // for future
//    fun pressedBtnConfirm(){
//        if (screenFirstState.value!!.isAnyAnswerActive){
//            _navigateToNextScreen.value = Unit
//            return
//        }
//        _statusMessage.value = SnackBarListener("Оберіть хоча б один із варіантів")
//    }
}