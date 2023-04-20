package com.example.taskaty.app.ui.fragments.auth.login

interface LoginView {

    fun showLoading()

    fun hideLoading()

    fun showErrorMessage(message: String)

    fun navigateToHomeScreen()

    fun showValidationError(message: String)
}