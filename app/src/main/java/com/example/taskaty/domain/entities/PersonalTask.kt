package com.example.taskaty.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class PersonalTask(
    override val id: String,
    override val title: String,
    override val description: String,
    override var status: Int,
    override val creationTime: String
) : Task,Parcelable {
    fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("title", title)
        jsonObject.put("description", description)
        jsonObject.put("status", status)
        jsonObject.put("creationTime", creationTime)
        return jsonObject.toString()
    }


}
