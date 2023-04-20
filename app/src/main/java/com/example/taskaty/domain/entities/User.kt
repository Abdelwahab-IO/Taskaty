package com.example.taskaty.domain.entities

import android.os.Parcelable
import com.example.taskaty.BuildConfig
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
    val name: String,
    val password: String,
    val teamId: String = BuildConfig.TEAM_ID
) :
    Parcelable {
        fun toJson():String{
            val jsonObject = JSONObject()
            jsonObject.put("name", name)
            jsonObject.put("password", password)
            jsonObject.put("teamId", teamId)
            return jsonObject.toString()
        }
}