package com.example.taskaty.data.api

import com.example.taskaty.BuildConfig
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.TeamTask
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class TasksApiClient(private val okHttpClient: OkHttpClient) {
	private val baseUrl = BuildConfig.BASE_URL


	fun getPersonalTasks(callback: Callback) {
		val request = Request.Builder()
			.url("$baseUrl/todo/personal").get()
			.build()
		okHttpClient.newCall(request).enqueue(callback)
	}

	fun addPersonalTask(callback: Callback, title: String, description: String) {
		val requestBody = MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("title", title)
			.addFormDataPart("description", description)
			.build()
		val request = Request.Builder().url("$baseUrl/todo/personal").post(requestBody).build()
		okHttpClient.newCall(request).enqueue(callback)
	}

	fun updatePersonalTask(callback: Callback, id: String, status: Int) {
		val requestBody = MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("id", id)
			.addFormDataPart("status", status.toString())
			.build()
		val request = Request.Builder().url("$baseUrl/todo/personal").put(requestBody).build()
		okHttpClient.newCall(request).enqueue(callback)

	}

	fun getTeamTasks(callback: Callback) {
		val request = Request.Builder()
			.url("$baseUrl/todo/team").get()
			.build()
		okHttpClient.newCall(request).enqueue(callback)


	}

	fun addTeamTask(
		callback: Callback, title: String,
		description: String,
		assignee: String
	) {
		val requestBody = MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("title", title)
			.addFormDataPart("description", description)
			.addFormDataPart("assignee", assignee)
			.build()
		val request = Request.Builder().url("$baseUrl/todo/team").post(requestBody).build()
		okHttpClient.newCall(request).enqueue(callback)
	}

	fun updateTeamTask(callback: Callback, id: String, status: Int) {
		val requestBody = MultipartBody.Builder()
			.setType(MultipartBody.FORM)
			.addFormDataPart("id", id)
			.addFormDataPart("status", status.toString())
			.build()
		val request = Request.Builder().url("$baseUrl/todo/team").put(requestBody).build()
		okHttpClient.newCall(request).enqueue(callback)

	}
}
