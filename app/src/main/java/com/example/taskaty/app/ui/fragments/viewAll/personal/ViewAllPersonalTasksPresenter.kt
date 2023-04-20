package com.example.taskaty.app.ui.fragments.viewAll.personal

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.interactors.PersonalTaskInteractor


class ViewAllPersonalTasksPresenter(
    private val personalTaskInteractor: PersonalTaskInteractor,
    private val view: ViewAllPersonalTasksView
)  {
     fun getPersonalTasks(state:Int) {
        view.showLoading()
        personalTaskInteractor.filterPersonalTasksStatus(state,object : RepoCallback<List<PersonalTask>> {
            override fun onSuccess(response: RepoResponse.Success<List<PersonalTask>>) {
                view.hideLoading()
                val tasks = response.data
                view.viewAllPersonalTasksStatus(state,tasks)
            }

            override fun onError(response: RepoResponse.Error<List<PersonalTask>>) {
                view.hideLoading()
                view.showErrorMessage(response.message)
            }
        } )
    }
}