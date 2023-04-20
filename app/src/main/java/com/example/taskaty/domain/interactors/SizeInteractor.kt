package com.example.taskaty.domain.interactors

import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.repositories.tasks.AllTasksRepository

class SizeInteractor(private val repo: AllTasksRepository) {

    fun sizeTasks(callback: RepoCallback<List<Task>>) {
        var isPersonalTaskLoaded = false
        var isTeamTasksLoaded = false
        var personalTasks: List<PersonalTask> = listOf()
        var teamTasks: List<TeamTask> = listOf()
        repo.getAllPersonalTasks(object : RepoCallback<List<PersonalTask>> {
            override fun onSuccess(response: RepoResponse.Success<List<PersonalTask>>) {
                isPersonalTaskLoaded = true
                personalTasks = response.data
                if (isTeamTasksLoaded)
                    callback.onSuccess(RepoResponse.Success(personalTasks + teamTasks))
            }

            override fun onError(response: RepoResponse.Error<List<PersonalTask>>) {
                callback.onError(RepoResponse.Error(response.message))
            }
        })
        repo.getAllTeamTasks(object : RepoCallback<List<TeamTask>> {
            override fun onSuccess(response: RepoResponse.Success<List<TeamTask>>) {
                isTeamTasksLoaded = true
                teamTasks = response.data
                if (isPersonalTaskLoaded)
                    callback.onSuccess(RepoResponse.Success(personalTasks + teamTasks))

            }

            override fun onError(response: RepoResponse.Error<List<TeamTask>>) {
                callback.onError(RepoResponse.Error(response.message))
            }

        })
    }
    fun clearDate(){
        repo.clearCashedData()
        AllTasksRepositoryImpl.clearInstance()
    }


}