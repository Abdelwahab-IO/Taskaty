package com.example.taskaty.app.ui.fragments.home.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.taskaty.R
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.databinding.FragmentAddTaskBottomSheetBinding
import com.example.taskaty.domain.interactors.PersonalTaskInteractor
import com.example.taskaty.domain.interactors.TeamTaskInteractor
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NewTaskBottomSheetFragment :
    BottomSheetDialogFragment(), NewTaskView {

    private lateinit var binding: FragmentAddTaskBottomSheetBinding
    private lateinit var presenter: NewTaskPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabId = arguments?.getInt(ARGUMENT_KEY)!!

        val allTasksRepositoryImpl = AllTasksRepositoryImpl.getInstance()
        presenter = NewTaskPresenter(
            this,
            PersonalTaskInteractor(allTasksRepositoryImpl),
            TeamTaskInteractor(allTasksRepositoryImpl)
        )
        setupAddTaskBottomSheet(tabId)
        addCallback(tabId)
        addTextFieldFocusListeners()
    }

    private fun setupAddTaskBottomSheet(tabId: Int) {
        if (tabId == TEAM_TAB_ID) {
            binding.title.setText(R.string.create_team_task)
            binding.assigneeTextInput.visibility = View.VISIBLE
        } else {
            binding.title.setText(R.string.create_personal_task)
            binding.assigneeTextInput.visibility = View.GONE
        }
    }

    private fun addCallback(tabId: Int) {
        binding.newTaskBtn.setOnClickListener {
            if (isValid(tabId)) {
                val title = binding.titleTextInput.editText!!.text.toString()
                val assignee = binding.assigneeTextInput.editText!!.text.toString().split(",")
                    .filter { it.isNotEmpty() }.joinToString(", ")
                val description = binding.descriptionTextInput.editText!!.text.toString()

                when (tabId) {
                    PERSONAL_TAB_ID -> {
                        presenter.addNewPersonalTask(title, description)
                    }

                    TEAM_TAB_ID -> {
                        presenter.addNewTeamTask(title, description, assignee)
                    }
                }
                enableButton(false)
            }
        }
    }

    private fun addTextFieldFocusListeners() {
        binding.titleTextInput.editText?.setOnFocusChangeListener { _, _ ->
            binding.titleTextInput.isErrorEnabled = false
        }
        binding.assigneeTextInput.editText?.setOnFocusChangeListener { _, _ ->
            binding.assigneeTextInput.isErrorEnabled = false
        }
        binding.descriptionTextInput.editText?.setOnFocusChangeListener { _, _ ->
            binding.descriptionTextInput.isErrorEnabled = false
        }
    }

    private fun isValid(tabId: Int): Boolean {
        var isValid = true

        if (binding.titleTextInput.editText!!.text.isEmpty()) {
            binding.titleTextInput.error = "Please Add Title"
            isValid = false
        } else {
            binding.titleTextInput.isErrorEnabled = false
        }

        if (binding.assigneeTextInput.editText!!.text.isEmpty() && tabId == TEAM_TAB_ID) {
            binding.assigneeTextInput.error = "Please Add at Least 1 Assignee"
            isValid = false
        } else {
            binding.assigneeTextInput.isErrorEnabled = false
        }

        if (binding.descriptionTextInput.editText!!.text.isEmpty()) {
            binding.descriptionTextInput.error = "Please Add Description"
            isValid = false
        } else {
            binding.descriptionTextInput.isErrorEnabled = false
        }

        return isValid
    }

    override fun showMessage(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun enableButton(isEnabled: Boolean) {
        binding.newTaskBtn.isEnabled = isEnabled
    }

    override fun closeBottomSheet() {
        dismiss()
    }

    companion object {
        private const val ARGUMENT_KEY = "tab_id"
        private const val PERSONAL_TAB_ID = 0
        private const val TEAM_TAB_ID = 1
        fun newInstance(tab_id: Int) = NewTaskBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putInt(ARGUMENT_KEY, tab_id)
            }
        }
    }
}