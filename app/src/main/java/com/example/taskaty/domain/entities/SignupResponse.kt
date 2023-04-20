package com.example.taskaty.domain.entities

import com.google.gson.annotations.SerializedName


data class SignupResponse(
    @SerializedName("value") val value: SignupValue,
    val message: Any?,
    val isSuccess: Boolean,
)

data class SignupValue(
    val userId: String,
    val username: String,
)