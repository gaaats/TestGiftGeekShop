package com.example.testgiftgeekshop.domain.usecases

import android.util.Log
import com.example.testgiftgeekshop.domain.MainRepository
import com.example.testgiftgeekshop.utils.Constances
import javax.inject.Inject

class FilterResultListByTitle @Inject constructor(
    private val repository: MainRepository
) {
    fun execute(listCodeFilters: List<Int>) {
        if (listCodeFilters.contains(Constances.ONLY_IN_GIFT_BOX)) {
            Log.d("test_params", "title filter is ONLY_IN_GIFT_BOX")
            repository.filterResultListByTitle(Constances.ONLY_IN_GIFT_BOX)
            return
        }
        Log.d("test_params", "title filter is: ONLY_WITHOUT_GIFT_BOX")
        repository.filterResultListByTitle(Constances.ONLY_WITHOUT_GIFT_BOX)
    }
}