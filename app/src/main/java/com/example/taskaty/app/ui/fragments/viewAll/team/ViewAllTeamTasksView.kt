package com.example.taskaty.app.ui.fragments.viewAll.team

import com.example.taskaty.domain.entities.TeamTask


interface ViewAllTeamTasksView {
    fun showLoading()
    fun hideLoading()
    fun showErrorMessage(message: String)
    fun viewAllTeamTasksStatus(state:Int,teamTasks: List<TeamTask>)
}