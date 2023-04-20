package com.example.taskaty.app.ui.fragments.auth.login

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.interactors.AuthInteractor

class LoginPresenter(
    private val authInteractor: AuthInteractor,
    private val view: LoginView
) {

     fun onLogin(userName: String, password: String) {
        if (!authInteractor.checkValidField(userName, password)) {
            view.showValidationError("Please fill all fields")
            return
        }
        view.showLoading()
        authInteractor.login(userName, password, object : RepoCallback<String> {
            override fun onSuccess(response: RepoResponse.Success<String>) {
                view.hideLoading()
                view.navigateToHomeScreen()
            }

            override fun onError(response: RepoResponse.Error<String>) {
                view.hideLoading()
                view.showErrorMessage(response.message)
            }

        })
    }

     fun onLoginWithSaveToken() {
        if (authInteractor.checkExpireToken()) {
            view.navigateToHomeScreen()
        }
    }
}
