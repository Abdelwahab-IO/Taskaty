package com.example.taskaty.app.ui.fragments.details.personal

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.interactors.PersonalTaskInteractor

class TaskDetailsPresenter(
    private val view: TaskDetailsView,
    private val personalTaskInteractor: PersonalTaskInteractor
)  {

	 fun getTask(taskId: String) {
		view.showGetTaskLoading()
		personalTaskInteractor.getTaskById(taskId, object : RepoCallback<PersonalTask> {
			override fun onSuccess(response: RepoResponse.Success<PersonalTask>) {
				view.hideGetTaskLoading()
				view.updateUiData(response.data)
			}

			override fun onError(response: RepoResponse.Error<PersonalTask>) {
				view.showMessage(response.message)
			}
		})

	}

	 fun updateTaskStatus(taskId: String, status: Int) {
		view.showUpdateTaskLoading()
		personalTaskInteractor.updateTaskStatus(taskId, status, object : RepoCallback<Unit> {
			override fun onSuccess(response: RepoResponse.Success<Unit>) {
				view.hideUpdateTaskLoading()
				view.showMessage("Task Updated Successfully")
			}

			override fun onError(response: RepoResponse.Error<Unit>) {
				view.hideUpdateTaskLoading()
				view.showMessage("Update Task Failed")
			}
		})

	}

}