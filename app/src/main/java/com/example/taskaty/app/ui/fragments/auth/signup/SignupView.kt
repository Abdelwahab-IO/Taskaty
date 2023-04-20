package com.example.taskaty.app.ui.fragments.auth.signup

interface SignupView {

    fun showLoading()

    fun hideLoading()

    fun showErrorMessage(message: String)

    fun navigateToLoginScreen(user: String)

    fun showValidationError(message: String)

}