package com.example.taskaty.app.ui.fragments.search

import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.interactors.SearchInteractor

class SearchPresenter(private val searchInteractor: SearchInteractor,private val view:SearchResualtView)  {
     fun searchTasks(query: String) {
        searchInteractor.searchTasks(query, object : RepoCallback<List<Task>> {
            override fun onSuccess(response: RepoResponse.Success<List<Task>>) {
                view.showResults(response.data)
            }

            override fun onError(response: RepoResponse.Error<List<Task>>) {
                view.showError(response.message)
            }
        })
    }
}