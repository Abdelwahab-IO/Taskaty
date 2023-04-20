package com.example.taskaty.domain.repositories.tasks

import com.example.taskaty.domain.repositories.tasks.PersonalTasksRepository
import com.example.taskaty.domain.repositories.tasks.TeamTasksRepository

interface AllTasksRepository: PersonalTasksRepository, TeamTasksRepository {
    fun clearCashedData()
}