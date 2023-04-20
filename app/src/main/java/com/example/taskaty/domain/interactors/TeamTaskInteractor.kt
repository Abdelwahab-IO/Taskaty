package com.example.taskaty.domain.interactors

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.repositories.tasks.TeamTasksRepository

class TeamTaskInteractor(
	private val teamTasksRepository: TeamTasksRepository,
) {

	fun getTeamTasksData(callback: RepoCallback<List<TeamTask>>) {
		teamTasksRepository.getAllTeamTasks(object : RepoCallback<List<TeamTask>> {
			override fun onSuccess(response: RepoResponse.Success<List<TeamTask>>) {
				val teamTask = response.data
				callback.onSuccess(RepoResponse.Success(teamTask))
			}

			override fun onError(response: RepoResponse.Error<List<TeamTask>>) {
				callback.onError(RepoResponse.Error(response.message))
			}
		})
	}

	fun getTeamTaskById(teamTaskId: String, callback: RepoCallback<TeamTask>) {
		teamTasksRepository.getAllTeamTasks(object : RepoCallback<List<TeamTask>> {
			override fun onSuccess(response: RepoResponse.Success<List<TeamTask>>) {
				val teamTask = response.data.find { it.id == teamTaskId }!!
				callback.onSuccess(RepoResponse.Success(teamTask))
			}

			override fun onError(response: RepoResponse.Error<List<TeamTask>>) {
				val message = response.message
				callback.onError(RepoResponse.Error(message))
			}
		})
	}

	fun updateTeamTaskStatus(
		teamTaskId: String,
		teamTaskStatus: Int,
		callback: RepoCallback<Unit>
	) {
		teamTasksRepository.updateTeamTaskState(
			teamTaskId,
			teamTaskStatus,
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

	fun addNewTeamTask(
		title: String,
		description: String,
		assignee: String,
		callback: RepoCallback<Unit>
	) {
		teamTasksRepository.createTeamTask(
			title,
			description,
			assignee,
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

	fun filterTeamTaskData(status: Int, callback: RepoCallback<List<TeamTask>>) {
		teamTasksRepository.getAllTeamTasks(object : RepoCallback<List<TeamTask>> {
			override fun onSuccess(response: RepoResponse.Success<List<TeamTask>>) {
				val teamTask = filterSates(status, response.data)
				callback.onSuccess(RepoResponse.Success(teamTask))
			}

			override fun onError(response: RepoResponse.Error<List<TeamTask>>) {
				val message = response.message
				callback.onError(RepoResponse.Error(message))
			}
		})
	}

	fun filterSates(status: Int, teamTask: List<TeamTask>): List<TeamTask> {
		return teamTask.filter { it.status == status }
	}
}