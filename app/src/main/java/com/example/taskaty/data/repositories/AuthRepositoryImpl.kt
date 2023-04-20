package com.example.taskaty.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.taskaty.data.api.UserApiClient
import com.example.taskaty.data.mappers.AuthMapper
import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.LoginResponse
import com.example.taskaty.domain.entities.SignupResponse
import com.example.taskaty.domain.entities.User
import com.example.taskaty.domain.repositories.auth.AuthRepository
import com.example.taskaty.global.GlobalState
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException

class AuthRepositoryImpl private constructor(private val application: Context) : AuthRepository {
    private var token: String? = null
    private var expireDate: String? = null


    override fun signup(user: User, callback: RepoCallback<SignupResponse>) {
        val userClient = UserApiClient(OkHttpClient())
        val apiCallBack = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RepoResponse.Error("No internet connection"))
            }

            override fun onResponse(call: Call, response: Response) {
                val signupResponse =
                    AuthMapper.mapJsonStringToSignupResponse(response.body.string())
                callback.onSuccess(RepoResponse.Success(signupResponse))
            }
        }
        userClient.signup(user, apiCallBack)
    }

    override fun login(
        userName: String,
        password: String,
        callback: RepoCallback<LoginResponse>
    ) {
        val userClient = UserApiClient(OkHttpClient())
        val apiCallBack = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RepoResponse.Error("No internet connection"))
            }

            override fun onResponse(call: Call, response: Response) {
                val loginResponse =
                    AuthMapper.mapJsonStringToLoginResponse(response.body.string())
                callback.onSuccess(RepoResponse.Success(loginResponse))
            }
        }
        userClient.login(userName, password, apiCallBack)
    }

    override fun logout() {
        updateToken("","")
    }

    override fun getToken(): String {
        if (token == null) {
            val sharedPreferences = getSharedPref()
            token = sharedPreferences.getString("token", "")
        }
        return token!!
    }

    override fun getExpirationDate(): String {
        if (expireDate == null) {
            val sharedPreferences = application.getSharedPreferences("auth", Context.MODE_PRIVATE)
            expireDate = sharedPreferences.getString("expireAt", "")
        }
        return expireDate!!
    }

    override fun updateToken(token: String, expireAt: String) {
        val sharedPreferences = getSharedPref()
        sharedPreferences.edit().putString("token", token).apply()
        sharedPreferences.edit().putString("expireAt", expireAt).apply()
        this.token = sharedPreferences.getString("token", "")
        this.expireDate = sharedPreferences.getString("expireAt", "")
    }
    private fun getSharedPref(): SharedPreferences =
        application.getSharedPreferences("auth", Context.MODE_PRIVATE)

    companion object {
        private var instance: AuthRepositoryImpl? = null

        fun getInstance(application: Context = GlobalState.appContext): AuthRepositoryImpl {
            if (instance == null) {
                instance = AuthRepositoryImpl(application)
            }
            return instance!!
        }
    }

}