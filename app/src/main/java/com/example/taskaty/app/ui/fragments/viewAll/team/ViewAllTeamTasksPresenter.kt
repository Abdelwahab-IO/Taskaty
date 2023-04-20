package com.example.taskaty.app.ui.fragments.viewAll.team

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.interactors.TeamTaskInteractor

class ViewAllTeamTasksPresenter(
    private val teamTaskInteractor: TeamTaskInteractor,
    private val view: ViewAllTeamTasksView
)  {
     fun getTeamTaskData(state:Int) {
        view.showLoading()
        teamTaskInteractor.filterTeamTaskData(state,object : RepoCallback<List<TeamTask>> {
            override fun onSuccess(response: RepoResponse.Success<List<TeamTask>>) {
                view.viewAllTeamTasksStatus(state,response.data)
                view.hideLoading()
            }

            override fun onError(response: RepoResponse.Error<List<TeamTask>>) {
                view.showErrorMessage(response.message)
                view.hideLoading()
            }


        })

    }
}