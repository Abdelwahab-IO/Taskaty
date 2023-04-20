package com.example.taskaty.app.ui.fragments.home.personal

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.interactors.PersonalTaskInteractor

class PersonalTasksPresenter(
    private val interactor: PersonalTaskInteractor,
    private val view: PersonalTasksView
) {
    fun getPersonalTasksData() {
        interactor.getPersonalTasksData(object : RepoCallback<List<PersonalTask>> {
            override fun onSuccess(response: RepoResponse.Success<List<PersonalTask>>) {
                view.filterTasks(response.data)
            }

            override fun onError(response: RepoResponse.Error<List<PersonalTask>>) {
                view.showErrorMessage(response.message)
            }
        })
    }

}