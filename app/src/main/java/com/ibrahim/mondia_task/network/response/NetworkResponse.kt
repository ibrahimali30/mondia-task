package com.ibrahim.mondia_task.network.response



data class NetworkResponse<T>(
    val successResponse: SuccessResponseCallBack<T> = SuccessResponseCallBack(),
    val errorResponse: ErrorResponseCallBack = ErrorResponseCallBack()
) {

    /**
     * cancelCall is set at @NetworkCaller<T> which will cancel the Future<*> object
     * in case a fired network request is needed to be canceled or disposed
     * **/
    var cancelCall: ()-> Unit = {}

    fun registerCallBack(onSuccess: (model: T) -> Unit, onFailure: (e: Exception) -> Unit) {
        successResponse.register(onSuccess)
        errorResponse.register(onFailure)
    }

    fun cancelNetworkCall() {
        cancelCall()
    }

}