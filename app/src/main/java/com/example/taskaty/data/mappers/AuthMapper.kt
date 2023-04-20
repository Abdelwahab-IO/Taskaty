package com.example.taskaty.data.mappers

import com.example.taskaty.domain.entities.LoginResponse
import com.example.taskaty.domain.entities.SignupResponse
import com.google.gson.Gson

object AuthMapper {
     fun mapJsonStringToLoginResponse(jsonString: String): LoginResponse {
        val gson = Gson()
        return gson.fromJson(jsonString, LoginResponse::class.java)
    }
     fun mapJsonStringToSignupResponse(jsonString: String): SignupResponse {
        val gson = Gson()
        return gson.fromJson(jsonString, SignupResponse::class.java)
    }
}