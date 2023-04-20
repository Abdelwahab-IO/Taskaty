package com.example.taskaty.app.ui.fragments.search

import com.example.taskaty.domain.entities.Task

interface SearchResualtView {
    fun showResults(results: List<Task>)
    fun showError(message: String)
}