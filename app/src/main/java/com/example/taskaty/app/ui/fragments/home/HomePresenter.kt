package com.example.taskaty.app.ui.fragments.home

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.interactors.AuthInteractor
import com.example.taskaty.domain.interactors.SizeInteractor

class HomePresenter(
    private val view: HomeView,
    private val sizeInteractor: SizeInteractor,
    private val authInteractor: AuthInteractor
)  {

     fun loadActiveTasksCount() {
        sizeInteractor.sizeTasks(object : RepoCallback<List<Task>> {
            override fun onSuccess(response: RepoResponse.Success<List<Task>>) {
                view.showActiveTasksCount(response.data.size)
            }
            override fun onError(response: RepoResponse.Error<List<Task>>) {
                view.showError(response.message)
            }
        })
    }
    fun onlogoutClicked() {
        sizeInteractor.clearDate()
        authInteractor.removeTokenFromLocal()
        view.navigateToLogin()
    }
}