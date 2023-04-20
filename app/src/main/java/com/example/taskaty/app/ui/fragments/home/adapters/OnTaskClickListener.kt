package com.example.taskaty.app.ui.fragments.home.adapters

import com.example.taskaty.domain.entities.Task

interface OnTaskClickListener {
    fun onTaskClick(task: Task)
}