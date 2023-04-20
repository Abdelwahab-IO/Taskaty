package com.example.taskaty.app.ui.fragments.home.bottomSheet


interface NewTaskView {
	fun showMessage(message: String)
	fun enableButton(isEnabled: Boolean)
	fun closeBottomSheet()

}