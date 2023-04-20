package com.example.taskaty.app.ui.fragments.viewAll.team

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.details.team.TeamTaskDetailsFragment
import com.example.taskaty.app.ui.fragments.home.adapters.OnTaskClickListener
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.databinding.FragmentViewAllTeamTasksBinding
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.domain.interactors.TeamTaskInteractor

class ViewAllTeamTasksFragment : BaseFragment<FragmentViewAllTeamTasksBinding>
    (FragmentViewAllTeamTasksBinding::inflate), ViewAllTeamTasksView {
    private lateinit var presenter: ViewAllTeamTasksPresenter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = ViewAllTeamTasksPresenter(
            TeamTaskInteractor(AllTasksRepositoryImpl.getInstance()), this
        )

        setup(requireArguments().getInt(TASK_TYPE_ARG))

    }

    private fun setup(status: Int) {
        presenter.getTeamTaskData(status)
        binding.toolbar.setNavigationOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun getStatusNames(status: Int?): String {
        return when (status) {
            0 -> "ToDo"
            1 -> "In Progress"
            else -> "Done"
        }
    }

    override fun showLoading() {
        requireActivity().runOnUiThread {
            binding.shimmerFrameLayout.startShimmer()
            binding.shimmerFrameLayout.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        requireActivity().runOnUiThread {
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.GONE
        }
    }

    override fun showErrorMessage(message: String) {
        Log.d("TAG", "showErrorMessage: $message")
    }

    override fun viewAllTeamTasksStatus(state: Int, teamTasks: List<TeamTask>) {
        requireActivity().runOnUiThread {
            val adapter = ViewAllTeamTasksAdapter(object : OnTaskClickListener {
                override fun onTaskClick(task: Task) {
                    val frag = TeamTaskDetailsFragment.getInstance(task.id)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container_fragment, frag).addToBackStack(null).commit()
                }

            })
            adapter.submitList(teamTasks)
            binding.textState.text = getStatusNames(state)
            binding.recyclerViewInViewAll.adapter = adapter
        }
    }

    companion object {
        private const val TASK_TYPE_ARG = "task_type"
        fun newInstance(taskType: Int) =
            ViewAllTeamTasksFragment().apply {
                arguments = Bundle().apply {
                    putInt(TASK_TYPE_ARG, taskType)
                }
            }
    }
}