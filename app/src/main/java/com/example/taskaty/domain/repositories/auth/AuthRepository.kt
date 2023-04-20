package com.example.taskaty.domain.repositories.auth

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.domain.entities.LoginResponse
import com.example.taskaty.domain.entities.SignupResponse
import com.example.taskaty.domain.entities.User

interface AuthRepository {

    fun getToken(): String

    fun getExpirationDate(): String

    fun updateToken(token: String, expireAt: String)
    fun signup(user: User, callback: RepoCallback<SignupResponse>)
    fun login(
        userName: String,
        password: String,
        callback: RepoCallback<LoginResponse>
    )
   fun logout()
}