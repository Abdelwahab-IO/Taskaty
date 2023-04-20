package com.example.taskaty.domain.interactors


import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.LoginResponse
import com.example.taskaty.domain.entities.SignupResponse
import com.example.taskaty.domain.entities.User
import com.example.taskaty.domain.repositories.auth.AuthRepository
import java.text.SimpleDateFormat
import java.util.Locale


class AuthInteractor(
    private val authRepository: AuthRepository,

) {

    fun login(userName: String, password: String, callback: RepoCallback<String>) {
        authRepository.login(userName,
            password,
            object : RepoCallback<LoginResponse> {

                override fun onSuccess(response: RepoResponse.Success<LoginResponse>) {
                    if (response.data.isSuccess) {
                        val token = response.data.value.token
                        val expireAt = response.data.value.expireAt
                        callback.onSuccess(RepoResponse.Success(token))
                        authRepository.updateToken(token, expireAt)
                    } else {
                        callback.onError(RepoResponse.Error(response.data.message.toString()))
                    }
                }

                override fun onError(response: RepoResponse.Error<LoginResponse>) {
                    callback.onError(RepoResponse.Error(response.message))
                }
            })
    }

    fun signup(user: User, callback: RepoCallback<String>) {
        authRepository.signup(user, object : RepoCallback<SignupResponse> {
            override fun onSuccess(response: RepoResponse.Success<SignupResponse>) {
                if (response.data.isSuccess) {
                    callback.onSuccess(RepoResponse.Success(response.data.value.username))
                } else {
                    callback.onError(RepoResponse.Error(response.data.message.toString()))
                }
            }

            override fun onError(response: RepoResponse.Error<SignupResponse>) {
                callback.onError(RepoResponse.Error(response.message))
            }


        })
    }

    private fun convertExpireTokenTime(expireAt: String): Long {
        if (expireAt.isEmpty()) return 0
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val expireAtDate = dateFormat.parse(expireAt)
        return expireAtDate!!.time
    }

    fun checkExpireToken(): Boolean {
        return if (convertExpireTokenTime(authRepository.getExpirationDate()) > System.currentTimeMillis()) {
            true
        } else {
            removeTokenFromLocal()
            false
        }
    }

     fun removeTokenFromLocal() {
        authRepository.updateToken("", "")
    }

    fun checkValidField(
        userName: String,
        password: String,
        confirmPassword: String = password
    ): Boolean {
        return userName.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    fun checkValidPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}


