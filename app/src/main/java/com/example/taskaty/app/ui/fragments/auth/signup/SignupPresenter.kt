package com.example.taskaty.app.ui.fragments.auth.signup

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.User
import com.example.taskaty.domain.interactors.AuthInteractor

class SignupPresenter(
    private val authInteractor: AuthInteractor,
    private val view: SignupView
) {

    fun onSignup(userName: String, password: String, confirmPassword: String) {
        if (!authInteractor.checkValidField(userName, password, confirmPassword)) {
            view.showValidationError("Please fill all fields")
            return
        }

        if (!authInteractor.checkValidPassword(password, confirmPassword)) {
            view.showValidationError("Passwords do not match")
            return
        }

        view.showLoading()
        authInteractor.signup(User(userName, password), object : RepoCallback<String> {
            override fun onSuccess(response: RepoResponse.Success<String>) {
                view.hideLoading()
                view.navigateToLoginScreen(response.data)
            }

            override fun onError(response: RepoResponse.Error<String>) {
                view.hideLoading()
                view.showErrorMessage(response.message)
            }
        })
    }
}
