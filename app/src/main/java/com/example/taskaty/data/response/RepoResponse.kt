package com.example.taskaty.data.response

sealed class RepoResponse<T> {
    data class Success<T>(val data: T) : RepoResponse<T>()
    data class Error<T>(val message: String) : RepoResponse<T>()
}

interface RepoCallback<T> {
    fun onSuccess(response: RepoResponse.Success<T>)
    fun onError(response: RepoResponse.Error<T>)
}