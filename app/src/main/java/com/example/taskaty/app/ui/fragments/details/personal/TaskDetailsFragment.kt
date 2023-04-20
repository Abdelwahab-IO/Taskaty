package com.example.taskaty.app.ui.fragments.details.personal

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.home.HomeFragment
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.databinding.FragmentTaskDetailsBinding
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.interactors.PersonalTaskInteractor
import com.example.taskaty.app.utils.DateTimeUtils


class TaskDetailsFragment : BaseFragment<FragmentTaskDetailsBinding>(
    FragmentTaskDetailsBinding::inflate
), TaskDetailsView {

    private lateinit var presenter: TaskDetailsPresenter
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var taskId: String

    private val statusMap = mapOf(
        UPCOMING_STATE to "Up Coming", IN_PROGRESS_STATE to "In Progress", DONE_STATE to "Done"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskId = arguments?.getString(ARGUMENT_KEY, "")!!
        val allTasksRepositoryImpl = AllTasksRepositoryImpl.getInstance()
        presenter = TaskDetailsPresenter(
            this, PersonalTaskInteractor(allTasksRepositoryImpl)
        )

        setup()
    }

    private fun setup() {
        presenter.getTask(taskId)
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
            presenter.updateTaskStatus(taskId, taskStatus)
        }

        binding.taskStatusButton.setOnClickListener {
            listPopupWindow.show()
        }
    }

    override fun updateUiData(task: PersonalTask) {
        requireActivity().runOnUiThread {
            with(binding) {
                binding.toolbarDetails.setNavigationOnClickListener {
                    requireActivity().supportFragmentManager.popBackStack()
                }
                taskTitle.text = task.title
                taskStatusButton.text = statusMap[task.status]
                taskDate.text = DateTimeUtils.toDateFormat(task.creationTime)
                taskTime.text = DateTimeUtils.toTimeFormat(task.creationTime)
                taskDescriptionContent.text = task.description
            }
        }
    }

    override fun showGetTaskLoading() {
        showLoading(progressBarVisible = false, taskDataVisible = false,shimmerVisible = true)
    }

    override fun hideGetTaskLoading() {
        showLoading(progressBarVisible = false, taskDataVisible = true,shimmerVisible = false)
    }

    override fun showUpdateTaskLoading() {
        showLoading(progressBarVisible = true, taskDataVisible = true,shimmerVisible = false)
    }

    override fun hideUpdateTaskLoading() {
        showLoading(progressBarVisible = false, taskDataVisible = true,shimmerVisible = false)
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(progressBarVisible: Boolean, taskDataVisible: Boolean,shimmerVisible:Boolean) {
        requireActivity().runOnUiThread {
            TransitionManager.beginDelayedTransition(binding.root)
            binding.progressBar.isVisible = progressBarVisible
            binding.taskDataLayout.isVisible = taskDataVisible
            binding.shimmerFrameLayout.isVisible = shimmerVisible
        }
    }

    companion object {
        private const val ARGUMENT_KEY = "personal_task_id"
        private const val UPCOMING_STATE = 0
        private const val IN_PROGRESS_STATE = 1
        private const val DONE_STATE = 2

        fun newInstance(taskId: String) = TaskDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_KEY, taskId)
            }
        }
    }


}
