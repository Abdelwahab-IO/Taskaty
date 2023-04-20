package com.example.taskaty.app.ui.fragments.details.team

import com.example.taskaty.domain.entities.TeamTask


interface TeamTaskDetailsView {

    fun showMessage(message: String)
    fun updateUiData(task: TeamTask)
    fun showGetTeamTaskLoading()
    fun hideGetTeamTaskLoading()
    fun showUpdateTeamTaskLoading()
    fun hideUpdateTeamTaskLoading()
}