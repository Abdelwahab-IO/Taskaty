package com.example.taskaty.data.api

import com.example.taskaty.BuildConfig
import com.example.taskaty.domain.entities.User
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class UserApiClient(private val okHttpClient: OkHttpClient) {
    private val baseURl = BuildConfig.BASE_URL

    fun login(userName: String, password: String, callback: Callback) {
        val base64Credentials = Credentials.basic(userName, password)
        val request = Request.Builder()
            .url("$baseURl/login")
            .header("Authorization",base64Credentials)
            .build()
       okHttpClient.newCall(request).enqueue(callback)
    }


    fun signup(user: User,callback: Callback) {
        val requestBody = FormBody.Builder()
            .add("username", user.name)
            .add("password", user.password)
            .add("teamId", user.teamId)
            .build()

        val request = Request.Builder()
            .url("$baseURl/signup")
            .post(requestBody)
            .build()
         okHttpClient.newCall(request).enqueue(callback)
    }

}