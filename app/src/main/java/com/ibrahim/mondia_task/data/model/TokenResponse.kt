package com.ibrahim.mondia_task.data.model

data class TokenResponse(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long
): NetworkResponseModel
