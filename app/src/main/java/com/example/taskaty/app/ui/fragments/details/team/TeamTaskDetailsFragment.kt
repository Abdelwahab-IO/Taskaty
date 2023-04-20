package com.example.taskaty.app.ui.fragments.details.team

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.data.repositories.AuthRepositoryImpl
import com.example.taskaty.databinding.FragmentTeamTaskDetailsBinding
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.interactors.TeamTaskInteractor
import com.example.taskaty.app.utils.DateTimeUtils


class TeamTaskDetailsFragment : BaseFragment<FragmentTeamTaskDetailsBinding>(
    FragmentTeamTaskDetailsBinding::inflate
), TeamTaskDetailsView {

    private lateinit var presenter: TeamTaskDetailsPresenter
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var teamTaskId: String

    private val statusMap = mapOf(
        UPCOMING_STATE to "Up Coming",
        IN_PROGRESS_STATE to "In Progress",
        DONE_STATE to "Done"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamTaskId = arguments?.getString(ARGUMENT_KEY, "")!!

        AuthRepositoryImpl.getInstance(requireActivity().application).getToken()

        val allTasksRepositoryImpl = AllTasksRepositoryImpl.getInstance()
        presenter = TeamTaskDetailsPresenter(
            this,
            TeamTaskInteractor(allTasksRepositoryImpl)
        )

        setup()
    }

    private fun setup() {
        presenter.getTeamTask(teamTaskId)
        initMenu()
        addCallbacks()

    }

    private fun initMenu() {
        listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.anchorView = binding.taskStatusButton

        val items = statusMap.values.toList()
        val adapter = ArrayAdapter(requireContext(), R.layout.item_task_state, items)
        listPopupWindow.setAdapter(adapter)
    }

    private fun addCallbacks() {
        var taskStatus = UPCOMING_STATE

        listPopupWindow.setOnItemClickListener { _, _, status: Int, _ ->
            binding.taskStatusButton.text = statusMap[status]
            taskStatus = status
            listPopupWindow.dismiss()
        }

        binding.submit.setOnClickListener {
            presenter.updateTeamTaskStatus(teamTaskId, taskStatus)
        }

        binding.taskStatusButton.setOnClickListener {
            listPopupWindow.show()
        }
    }

    override fun updateUiData(task: TeamTask) {
        requireActivity().runOnUiThread {
            with(binding) {
                toolbarDetails.setNavigationOnClickListener {
                    requireActivity().supportFragmentManager.popBackStack()
                }
                taskTitle.text = task.title
                taskStatusButton.text = statusMap[task.status]
                taskDate.text = DateTimeUtils.toDateFormat(task.creationTime)
                taskTime.text = DateTimeUtils.toTimeFormat(task.creationTime)
                taskDescriptionContent.text = task.description
                taskTeam.text = task.assignee
            }
        }
    }

    override fun showGetTeamTaskLoading() {
        showLoading(progressBarVisible = false, taskDataVisible = false, shimmerVisible = true)
    }

    override fun hideGetTeamTaskLoading() {
        showLoading(progressBarVisible = false, taskDataVisible = true, shimmerVisible = false)
    }

    override fun showUpdateTeamTaskLoading() {
        showLoading(progressBarVisible = true, taskDataVisible = true, shimmerVisible = false)
    }

    override fun hideUpdateTeamTaskLoading() {
        showLoading(progressBarVisible = false, taskDataVisible = true, shimmerVisible = false)
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(
        progressBarVisible: Boolean,
        taskDataVisible: Boolean,
        shimmerVisible: Boolean
    ) {
        requireActivity().runOnUiThread {
            TransitionManager.beginDelayedTransition(binding.root)
            binding.progressBar.isVisible = progressBarVisible
            binding.taskDataLayout.isVisible = taskDataVisible
            binding.shimmerFrameLayout.isVisible = shimmerVisible
        }
    }


    companion object {
        private const val ARGUMENT_KEY = "team_task_id"
        private const val UPCOMING_STATE = 0
        private const val IN_PROGRESS_STATE = 1
        private const val DONE_STATE = 2

        fun getInstance(teamTaskId: String) = TeamTaskDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_KEY, teamTaskId)
            }
        }
    }


}
