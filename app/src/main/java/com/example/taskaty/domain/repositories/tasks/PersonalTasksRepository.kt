package com.example.taskaty.domain.repositories.tasks

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.domain.entities.PersonalTask

interface PersonalTasksRepository {
    fun getAllPersonalTasks(callback: RepoCallback<List<PersonalTask>>)
    fun createPersonalTask(title: String, description: String, callback: RepoCallback<Unit>)
    fun updatePersonalTaskState(taskId: String, status: Int, callback: RepoCallback<Unit>)
}
