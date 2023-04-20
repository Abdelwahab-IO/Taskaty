package com.example.taskaty.domain.entities

interface Task {
    val id: String
    val title: String
    val description: String
    var status: Int
    val creationTime: String
}