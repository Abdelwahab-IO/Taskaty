package com.example.taskaty.app.ui.fragments.home

interface HomeView {
    fun showActiveTasksCount(count: Int)
    fun showError(errorMessage: String)
    fun navigateToLogin()
}