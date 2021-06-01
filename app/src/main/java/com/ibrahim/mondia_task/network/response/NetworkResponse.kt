package com.ibrahim.mondia_task.network.response

import com.ibrahim.mondia_task.data.model.NetworkResponseModel

data class NetworkResponse<T: NetworkResponseModel>(
    val sucess: SuccessResponseCallBack<T> = SuccessResponseCallBack(),
    val error: ErrorResponseCallBack = ErrorResponseCallBack()
) {

    var cancelCall: ()-> Unit = {}

    fun registerCallBack(onSuccess: (model: T) -> Unit, onFailure: (e: Exception) -> Unit) {
        sucess.register(onSuccess)
        error.register(onFailure)
    }

    fun cancel() {
        cancelCall()
    }

}