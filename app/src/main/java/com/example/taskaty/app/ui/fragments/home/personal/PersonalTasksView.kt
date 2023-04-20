package com.example.taskaty.app.ui.fragments.home.personal

import com.example.taskaty.domain.entities.PersonalTask

interface PersonalTasksView {

    fun filterTasks(tasks: List<PersonalTask>)

    fun showErrorMessage(message: String)

}