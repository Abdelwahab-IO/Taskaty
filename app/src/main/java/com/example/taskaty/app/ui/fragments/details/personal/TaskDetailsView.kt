package com.example.taskaty.app.ui.fragments.details.personal

import com.example.taskaty.domain.entities.PersonalTask


interface TaskDetailsView {

    fun showMessage(message: String)

    fun updateUiData(task: PersonalTask)

    fun showGetTaskLoading()

    fun hideGetTaskLoading()

    fun showUpdateTaskLoading()

    fun hideUpdateTaskLoading()

}