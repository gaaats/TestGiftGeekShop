package com.example.testgiftgeekshop.data

import android.util.Log
import com.example.geekshopappbuy.data.entity.products.ResponseProductList
import com.example.geekshopappbuy.data.entity.singleproduct.ResponseSingleProduct
import com.example.geekshopappbuy.domain.entitys.GeekProductUI
import com.example.testgiftgeekshop.domain.MainRepository
import com.example.testgiftgeekshop.utils.Constances
import com.example.testgiftgeekshop.utils.Mapper
import com.example.testgiftgeekshop.utils.ResourceVrap
import com.example.testgiftgeekshop.utils.SimpleResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepoImpl @Inject constructor(
    private val apiService: PromAPI,
) : MainRepository {

    val mainList = mutableListOf<GeekProductUI>()

    override suspend fun getProductInfo(productId: Int): SimpleResponse<ResponseSingleProduct> {
        return safeApiCall { apiService.getProductInfo(productId) }
    }

    // for one product load
    override suspend fun getAllProductsByIDs(listParams: List<Int>): ResourceVrap<List<GeekProductUI>> {
        try {
            if (listParams.isEmpty()) return ResourceVrap.Error(RuntimeException("mainList is Empty"))
            listParams.forEach {
                addProductToList(it)
            }
            return ResourceVrap.Success(mainList)
        } catch (e: Exception) {
            return (ResourceVrap.Error(exception = e))
        }
    }

    // for group load
    override suspend fun getAllProductsByGroupIds(listGroups: List<Int>): ResourceVrap<List<GeekProductUI>> {
        clearMainListBeforeLoad()
        try {
            if (listGroups.isEmpty()) return ResourceVrap.Error(RuntimeException("listGroups is Empty"))
            listGroups.forEach {
                val result = getSingleGroupProductsByGroupID(it)

                //test
                /*
                result.data!!.body()!!.products!!.forEach {
                    Log.d(
                        "test_params",
                        "Element name is  ${it!!.name}, status:${it.status}, presence:${it.presence}, presenceSure:${it.presenceSure}"
                    )
                }

                 */

                Mapper.mapGeekResponseProductListToUIList(result).also { list ->
                    mainList.addAll(list)

                    //todo: add it after all elements added
//                    filterByAvailable()
                }
            }
            filterByAvailable()
            return ResourceVrap.Success(mainList)
        } catch (e: Exception) {
            return (ResourceVrap.Error(exception = e))
        }
    }

    override fun filterResultListByPrice(maxPrice: Int) {
        mainList.removeIf {
            it.price >= maxPrice
        }
    }

    override fun filterResultListByTitle(params: Int) {
        if (params == Constances.ONLY_IN_GIFT_BOX) {
            mainList.removeIf {
                Log.d("test_params", "MAINREPO inside name ${it.name}")
                Log.d("test_params", "MAINREPO inside is: ${it.name!!.contains("бокс", true)}")
                !it.name!!.contains("бокс", true)
            }
        }
        if (params == Constances.ONLY_WITHOUT_GIFT_BOX) {
            Log.d("test_params", "MAINREPO title filter is ONLY_IN_GIFT_BOX")
            mainList.removeIf {
                it.name!!.contains("бокс", true)
            }
        }
    }

    private fun filterByAvailable() {
        mainList.removeIf {
            it.status == Constances.ITEM_NOT_DISPLAY || it.presence == Constances.ITEM_NOT_AVAILABLE
        }
    }

    private fun clearMainListBeforeLoad() {
        mainList.clear()
    }

    // for one product load
    private suspend fun addProductToList(productId: Int) {
        val resultElement = getProductInfo(productId)
        if (resultElement.isSuccessful) {
            Mapper.mapProductFormPromModelToUi(
                resultElement.body.product
                    ?: throw RuntimeException("empty element in MainRepo - addProductToList")
            ).also {
                mainList.add(it)
            }
        } else {
            throw RuntimeException("empty element in MainRepo - addProductToList")
        }
    }

    //load products by groupID
    private suspend fun getSingleGroupProductsByGroupID(groupId: Int): SimpleResponse<ResponseProductList> {
        return safeApiCall { apiService.getProductsByGroupID(groupId) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>): SimpleResponse<T> {
        return try {
            Log.d("TEST_TAG", "Im RepoImpl --- success")
            SimpleResponse.success(apiCall.invoke())
        } catch (e: Exception) {
            Log.d("TEST_TAG", "Im RepoImpl --- catch")
            SimpleResponse.failure(e)
        }
    }
}