package com.example.taskaty.app.ui.fragments.details.team

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.interactors.TeamTaskInteractor

class TeamTaskDetailsPresenter(
    private val view: TeamTaskDetailsView,
    private val teamTaskInteractor: TeamTaskInteractor
) {

	 fun getTeamTask(teamTaskId: String) {
		view.showGetTeamTaskLoading()
		teamTaskInteractor.getTeamTaskById(teamTaskId, object : RepoCallback<TeamTask> {
			override fun onSuccess(response: RepoResponse.Success<TeamTask>) {
				view.hideGetTeamTaskLoading()
				view.updateUiData(response.data)
			}

			override fun onError(response: RepoResponse.Error<TeamTask>) {
				view.showMessage(response.message)
			}
		})

	}

	 fun updateTeamTaskStatus(teamTaskId: String, status: Int) {
		view.showUpdateTeamTaskLoading()
		teamTaskInteractor.updateTeamTaskStatus(teamTaskId, status, object : RepoCallback<Unit> {
			override fun onSuccess(response: RepoResponse.Success<Unit>) {
				view.hideUpdateTeamTaskLoading()
				view.showMessage("Team Task Updated Successfully")
			}

			override fun onError(response: RepoResponse.Error<Unit>) {
				view.hideUpdateTeamTaskLoading()
				view.showMessage("Update Team Task Failed")
			}
		})

	}

}