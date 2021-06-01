package com.ibrahim.mondia_task.network.response

class SuccessResponseCallBack<T> {
    val functions = mutableListOf<(result: T) -> Unit>()

    fun register(func: (result: T) -> Unit) {
        functions.add(func)
    }

    fun invoke(data: T) {
        functions.forEach {
            it.invoke(data)
        }
    }
}