package com.example.taskaty.data.mappers

import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.TeamTask
import com.google.gson.Gson
import com.google.gson.JsonObject

object TaskMappers {

    fun jsonToTeamTasks(jsonString: String): List<TeamTask> {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        val jsonTasks = jsonObject.getAsJsonArray("value")
        val tasks = mutableListOf<TeamTask>()
        for (jsonElement in jsonTasks) {
            val jsonTask = jsonElement.asJsonObject
            val task = jsonToTeamTask(jsonTask)
            tasks.add(task)
        }
        return tasks
    }

    private fun jsonToTeamTask(json: JsonObject): TeamTask {
        val gson = Gson()
        val taskJson = gson.fromJson(json, TeamTask::class.java)

        return TeamTask(
            taskJson.id,
            taskJson.title,
            taskJson.description,
            taskJson.status,
            taskJson.creationTime,
            taskJson.assignee
        )

    }

    fun jsonToTasks(json: String): List<PersonalTask> {
        val gson = Gson()
        val tasksJson = gson.fromJson(json, JsonObject::class.java)
        val tasksArray = tasksJson.asJsonObject.getAsJsonArray("value")
        val tasksList = mutableListOf<PersonalTask>()

        tasksArray.forEach { taskJson ->
            tasksList.add(mapToTask(taskJson.toString()))
        }

        return tasksList
    }

    private fun mapToTask(json: String): PersonalTask {
        val gson = Gson()
        val taskJson = gson.fromJson(json, PersonalTask::class.java)

        return PersonalTask(
            taskJson.id,
            taskJson.title,
            taskJson.description,
            taskJson.status,
            taskJson.creationTime
        )
    }

    fun jsonToPersonalTask(json: String): PersonalTask {
        val gson = Gson()
        val responseJson = gson.fromJson(json, JsonObject::class.java)

        val taskJson = responseJson.getAsJsonObject("value")

        return PersonalTask(
            taskJson.get("id").asString,
            taskJson.get("title").asString,
            taskJson.get("description").asString,
            taskJson.get("status").asInt,
            taskJson.get("creationTime").asString
        )
    }

    fun jsonToTeamTask(json: String): TeamTask {
        val gson = Gson()
        val responseJson = gson.fromJson(json, JsonObject::class.java)

        val taskJson = responseJson.getAsJsonObject("value")

        return TeamTask(
            id = taskJson.get("id").asString,
            title = taskJson.get("title").asString,
            description = taskJson.get("description").asString,
            assignee = taskJson.get("assignee").asString,
            status = taskJson.get("status").asInt,
            creationTime = taskJson.get("creationTime").asString
        )
    }
}