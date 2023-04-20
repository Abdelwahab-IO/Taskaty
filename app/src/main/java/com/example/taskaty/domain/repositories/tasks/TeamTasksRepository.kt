package com.example.taskaty.domain.repositories.tasks

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.domain.entities.TeamTask

interface TeamTasksRepository{
    fun getAllTeamTasks(callback: RepoCallback<List<TeamTask>>)
    fun createTeamTask(title: String,
                       description: String,
                       assignee: String, callback: RepoCallback<Unit>)
    fun updateTeamTaskState(taskId: String, status: Int, callback: RepoCallback<Unit>)
}