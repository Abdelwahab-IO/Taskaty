package com.example.taskaty.data.repositories

import com.example.taskaty.data.api.TasksApiClient
import com.example.taskaty.data.api.interceptors.AuthInterceptor
import com.example.taskaty.data.mappers.TaskMappers
import com.example.taskaty.data.mappers.TaskMappers.jsonToPersonalTask
import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.repositories.tasks.AllTasksRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException

class AllTasksRepositoryImpl private constructor() : AllTasksRepository {

    private val authRepo: AuthRepositoryImpl by lazy { AuthRepositoryImpl.getInstance() }
    private val userToken by lazy { authRepo.getToken() }
    private val okHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(AuthInterceptor(userToken)).build()
    }
    private val tasksApiClient by lazy { TasksApiClient(okHttpClient) }
    private var cachedPersonalTasks: List<PersonalTask> = listOf()
    private var cachedTeamTasks: List<TeamTask> = listOf()
    override fun clearCashedData() {
        cachedPersonalTasks = listOf()
        cachedTeamTasks = listOf()
    }


    override fun getAllPersonalTasks(callback: RepoCallback<List<PersonalTask>>) {

        if (cachedPersonalTasks.isEmpty())
            tasksApiClient.getPersonalTasks(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(RepoResponse.Error(e.toString()))
                }

                override fun onResponse(call: Call, response: Response) {
                    cachedPersonalTasks = TaskMappers.jsonToTasks(response.body.string())
                    callback.onSuccess(RepoResponse.Success(cachedPersonalTasks))
                }
            })
        else
            callback.onSuccess(RepoResponse.Success(cachedPersonalTasks))
    }

    override fun createPersonalTask(
        title: String,
        description: String,
        callback: RepoCallback<Unit>
    ) {
        tasksApiClient.addPersonalTask(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RepoResponse.Error(e.toString()))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body.string()
                val createdTask = jsonToPersonalTask(responseBody)
                cachedPersonalTasks = cachedPersonalTasks + createdTask
                callback.onSuccess(RepoResponse.Success(Unit))
            }
        }, title, description)
    }


    override fun updatePersonalTaskState(
        taskId: String,
        status: Int,
        callback: RepoCallback<Unit>
    ) {
        tasksApiClient.updatePersonalTask(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RepoResponse.Error(e.toString()))
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(RepoResponse.Success(Unit))
                updateCachedData(taskId, status, cachedPersonalTasks)

            }
        }, taskId, status)
    }

    //
    override fun getAllTeamTasks(callback: RepoCallback<List<TeamTask>>) {
        if (cachedTeamTasks.isEmpty())
            tasksApiClient.getTeamTasks(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(RepoResponse.Error(e.toString()))
                }

                override fun onResponse(call: Call, response: Response) {
                    cachedTeamTasks = TaskMappers.jsonToTeamTasks(response.body.string())
                    callback.onSuccess(RepoResponse.Success(cachedTeamTasks))
                }
            })
        else
            callback.onSuccess(RepoResponse.Success(cachedTeamTasks))
    }

    override fun createTeamTask(
        title: String,
        description: String,
        assignee: String, callback: RepoCallback<Unit>
    ) {
        tasksApiClient.addTeamTask(
            object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RepoResponse.Error(e.toString()))
            }

            override fun onResponse(call: Call, response: Response) {
                val createdTask = TaskMappers.jsonToTeamTask(response.body.string())
                cachedTeamTasks = cachedTeamTasks + createdTask
                callback.onSuccess(RepoResponse.Success(Unit))
            }
        }, title, description, assignee)
    }

    override fun updateTeamTaskState(
        taskId: String,
        status: Int,
        callback: RepoCallback<Unit>
    ) {
        tasksApiClient.updateTeamTask(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(RepoResponse.Error(e.toString()))
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess(RepoResponse.Success(Unit))
                updateCachedData(taskId, status, cachedTeamTasks)
            }
        }, taskId, status)
    }

    private fun <T : Task> updateCachedData(id: String, status: Int, data: List<T>) {
        val taskIndex = data.indexOfFirst { it.id == id }
        data[taskIndex].status = status

    }


    companion object {
        private var instance: AllTasksRepositoryImpl? = null

        fun getInstance(): AllTasksRepositoryImpl {
            if (instance == null) {
                instance = AllTasksRepositoryImpl()
            }
            return instance!!
        }
        fun clearInstance() {
            instance = null
        }
    }
}
