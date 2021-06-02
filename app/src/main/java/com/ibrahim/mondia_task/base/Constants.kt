package com.ibrahim.mondia_task.base

object Global{
    var token = ""
    get() {
        return "Bearer $field"
    }
    var gateway_header_key = "X-MM-GATEWAY-KEY"
    var gateway_header_value = "Ge6c853cf-5593-a196-efdb-e3fd7b881eca"
    var token_header_key = "Authorization"

}