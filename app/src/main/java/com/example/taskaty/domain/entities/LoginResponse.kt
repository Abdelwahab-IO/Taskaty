package com.example.taskaty.domain.entities

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("value")val value: LoginValue,
    val message: Any?,
    val isSuccess: Boolean
)
data class LoginValue(
    val token: String,
    val expireAt: String
)
