package com.example.taskaty.app.ui.fragments.viewAll.personal

import com.example.taskaty.domain.entities.Task

interface ViewAllPersonalTasksView {

    fun showLoading()
    fun hideLoading()
    fun showErrorMessage(message: String)
    fun viewAllPersonalTasksStatus(state:Int,tasks: List<Task>)

}