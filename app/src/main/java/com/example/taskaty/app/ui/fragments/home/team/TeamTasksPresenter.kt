package com.example.taskaty.app.ui.fragments.home.team

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.interactors.TeamTaskInteractor

class TeamTasksPresenter(
    private val interactor: TeamTaskInteractor,
    private val view: TeamTasksView
) {

    fun getTeamTasksData() {
        interactor.getTeamTasksData(object : RepoCallback<List<TeamTask>> {
            override fun onSuccess(response: RepoResponse.Success<List<TeamTask>>) {
                view.filterTasks(response.data)
            }

            override fun onError(response: RepoResponse.Error<List<TeamTask>>) {
                view.showErrorMessage(response.message)
            }
        })
    }

}
