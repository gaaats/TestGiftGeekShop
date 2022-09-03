package com.geekshop.testgiftgeekshop.utils

import com.geekshop.testgiftgeekshop.data.entity.ErrorInputModel
import com.geekshop.testgiftgeekshop.domain.entity.OrderEntity

// A generic class that contains data and status about loading this data.
sealed class ResourceVrap<T>(
    val data: T? = null,
    val exception: Throwable? = null,
) {
    class Success<T>(data: T) : ResourceVrap<T>(data)
    class Error<T>(exception: Throwable, data: T? = null) :
        ResourceVrap<T>(exception = exception, data = data)

    class Loading<T>(data: T? = null) : ResourceVrap<T>(data)
}

sealed class OrderStatusVrapper(
    val data: OrderEntity? = null,
    val error: ErrorInputModel? = null,
) {
    val safeData: OrderEntity
        get() = data!!

    val safeError: ErrorInputModel
        get() = error!!

    class Success(data: OrderEntity) : OrderStatusVrapper(data = data)
    class Error(error: ErrorInputModel) : OrderStatusVrapper(error = error)
    object InProcess : OrderStatusVrapper()
}