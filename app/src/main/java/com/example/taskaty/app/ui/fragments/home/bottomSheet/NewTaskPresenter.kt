package com.example.taskaty.app.ui.fragments.home.bottomSheet

import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.interactors.PersonalTaskInteractor
import com.example.taskaty.domain.interactors.TeamTaskInteractor
import com.example.taskaty.domain.repositories.tasks.PersonalTasksRepository
import com.example.taskaty.domain.repositories.tasks.TeamTasksRepository

class NewTaskPresenter(
	private val newTaskView: NewTaskView,
	private val personalTaskInteractor: PersonalTaskInteractor,
	private val teamTaskInteractor: TeamTaskInteractor,
) {

	fun addNewPersonalTask(title: String, description: String) {
		newTaskView.enableButton(false)
		personalTaskInteractor.addNewTask(title, description, object : RepoCallback<Unit> {
			override fun onSuccess(response: RepoResponse.Success<Unit>) {
				newTaskView.showMessage("Personal Task Created Successfully")
				newTaskView.closeBottomSheet()
			}

			override fun onError(response: RepoResponse.Error<Unit>) {
				val message = response.message
				newTaskView.showMessage("Personal Task cannot be Created $message")
				newTaskView.enableButton(true)
			}

		})
	}

	fun addNewTeamTask(title: String, description: String, assignee: String) {
		newTaskView.enableButton(false)
		teamTaskInteractor.addNewTeamTask(
			title,
			description,
			assignee,
			object : RepoCallback<Unit> {
				override fun onSuccess(response: RepoResponse.Success<Unit>) {
					newTaskView.showMessage("Team Task Created Successfully")
					newTaskView.closeBottomSheet()
				}

				override fun onError(response: RepoResponse.Error<Unit>) {
					val message = response.message
					newTaskView.showMessage("Team Task cannot be Created $message")
					newTaskView.enableButton(true)
				}
			})
	}
}