package com.ibrahim.mondia_task.network.response

class ErrorResponseCallBack {
    private val functions = mutableListOf<(result: Exception) -> Unit>()

    fun register(func: (result: Exception) -> Unit) {
        functions.add(func)
    }

    fun invoke(data: java.lang.Exception) {
        functions.forEach {
            it.invoke(data)
        }
    }
}
