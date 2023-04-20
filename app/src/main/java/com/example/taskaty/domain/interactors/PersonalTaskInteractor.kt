package com.example.taskaty.domain.interactors

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.repositories.tasks.PersonalTasksRepository

class PersonalTaskInteractor(
	private val personalTasksRepository: PersonalTasksRepository,
) {

	fun getPersonalTasksData(callback: RepoCallback<List<PersonalTask>>) {
		personalTasksRepository.getAllPersonalTasks(object : RepoCallback<List<PersonalTask>> {
			override fun onSuccess(response: RepoResponse.Success<List<PersonalTask>>) {
				val task = response.data
				callback.onSuccess(RepoResponse.Success(task))
			}

			override fun onError(response: RepoResponse.Error<List<PersonalTask>>) {
				callback.onError(RepoResponse.Error(response.message))
			}
		})
	}


	fun getTaskById(taskId: String, callback: RepoCallback<PersonalTask>) {
		personalTasksRepository.getAllPersonalTasks(object : RepoCallback<List<PersonalTask>> {
			override fun onSuccess(response: RepoResponse.Success<List<PersonalTask>>) {
				val task = response.data.find { it.id == taskId }!!
				callback.onSuccess(RepoResponse.Success(task))
			}

			override fun onError(response: RepoResponse.Error<List<PersonalTask>>) {
				val message = response.message
				callback.onError(RepoResponse.Error(message))
			}
		})
	}

	fun updateTaskStatus(taskId: String, taskStatus: Int, callback: RepoCallback<Unit>) {
		personalTasksRepository.updatePersonalTaskState(
			taskId,
			taskStatus,
			object : RepoCallback<Unit> {
				override fun onSuccess(response: RepoResponse.Success<Unit>) {
					callback.onSuccess(RepoResponse.Success(Unit))
				}

				override fun onError(response: RepoResponse.Error<Unit>) {
					val message = response.message
					callback.onError(RepoResponse.Error(message))
				}
			})
	}

	fun addNewTask(title: String, description: String, callback: RepoCallback<Unit>) {
		personalTasksRepository.createPersonalTask(title, description, object : RepoCallback<Unit> {
			override fun onSuccess(response: RepoResponse.Success<Unit>) {
				callback.onSuccess(RepoResponse.Success(Unit))
			}

			override fun onError(response: RepoResponse.Error<Unit>) {
				val message = response.message
				callback.onError(RepoResponse.Error(message))
			}
		})
	}

	fun filterPersonalTasksStatus(state: Int, callback: RepoCallback<List<PersonalTask>>) {
		personalTasksRepository.getAllPersonalTasks(object : RepoCallback<List<PersonalTask>> {
			override fun onSuccess(response: RepoResponse.Success<List<PersonalTask>>) {
				val task = filterSates(state, response.data)
				callback.onSuccess(RepoResponse.Success(task))
			}

			override fun onError(response: RepoResponse.Error<List<PersonalTask>>) {
				callback.onError(RepoResponse.Error(response.message))
			}
		})
	}

	fun filterSates(state: Int, personalTask: List<PersonalTask>): List<PersonalTask> {
		return personalTask.filter { it.status == state }
	}
}

