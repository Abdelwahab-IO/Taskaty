package com.example.taskaty.app.ui.fragments.home.adapters

import com.example.taskaty.domain.entities.Task

interface SearchListener {
    fun onClick(task: Task)
}