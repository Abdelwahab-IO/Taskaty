package com.example.taskaty.app.ui.fragments.home.team

import com.example.taskaty.domain.entities.TeamTask

interface TeamTasksView {

    fun filterTasks(tasks: List<TeamTask>)

    fun showErrorMessage(message: String)

}