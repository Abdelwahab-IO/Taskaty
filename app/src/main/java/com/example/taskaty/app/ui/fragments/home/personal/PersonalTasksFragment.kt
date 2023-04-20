package com.example.taskaty.app.ui.fragments.home.personal

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.details.personal.TaskDetailsFragment
import com.example.taskaty.app.ui.fragments.home.adapters.OnTaskClickListener
import com.example.taskaty.app.ui.fragments.home.adapters.OnViewAllClickListener
import com.example.taskaty.app.ui.fragments.home.adapters.ParentPersonalAdapter
import com.example.taskaty.app.ui.fragments.viewAll.personal.ViewAllPersonalTasksFragment
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.databinding.FragmentPersonalTasksBinding
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.interactors.PersonalTaskInteractor


class PersonalTasksFragment :
    BaseFragment<FragmentPersonalTasksBinding>(FragmentPersonalTasksBinding::inflate),
    PersonalTasksView {

    lateinit var presenter: PersonalTasksPresenter
    private val interactor =
        PersonalTaskInteractor(AllTasksRepositoryImpl.getInstance())
    private var inProgressTasks = listOf<PersonalTask>()
    private var upcomingTasks = listOf<PersonalTask>()
    private var doneTasks = listOf<PersonalTask>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = PersonalTasksPresenter(interactor, this)
        presenter.getPersonalTasksData()
    }


    override fun filterTasks(tasks: List<PersonalTask>) {
        requireActivity().runOnUiThread {
            inProgressTasks = tasks.filter { it.status == IN_PROGRESS_STATUS }
            upcomingTasks = tasks.filter { it.status == UPCOMING_STATUS }
            doneTasks = tasks.filter { it.status == DONE_STATUS }
            initViews()
        }
    }

    override fun showErrorMessage(message: String) {
        requireActivity().runOnUiThread {
            binding.apply {
                progressBar.isVisible = false
                errorText.isVisible = true
                errorText.text = message
            }
        }
    }

    private fun initViews() {
        val adapter = ParentPersonalAdapter(
            inProgressTasks,
            upcomingTasks,
            doneTasks,
            object : OnViewAllClickListener {
                override fun onViewAllClick(taskType: Int) {
                    val frag = ViewAllPersonalTasksFragment.newInstance(taskType)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container_fragment, frag).addToBackStack(null).commit()
                }
            },
            object : OnTaskClickListener {
                override fun onTaskClick(task: Task) {
                    val frag = TaskDetailsFragment.newInstance(task.id)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container_fragment, frag).addToBackStack(null).commit()
                }

            })
        requireActivity().runOnUiThread {
            binding.PersonalTasksRecycler.adapter = adapter
        }
        showTasks()
    }

    private fun showTasks() {
        binding.apply {
            progressBar.isVisible = false
            PersonalTasksRecycler.isVisible = true
        }
    }


    companion object {
        const val UPCOMING_STATUS = 0
        const val IN_PROGRESS_STATUS = 1
        const val DONE_STATUS = 2
    }
}