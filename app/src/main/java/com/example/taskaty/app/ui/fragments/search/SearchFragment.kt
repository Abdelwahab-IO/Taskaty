package com.example.taskaty.app.ui.fragments.search


import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.details.personal.TaskDetailsFragment
import com.example.taskaty.app.ui.fragments.details.team.TeamTaskDetailsFragment
import com.example.taskaty.app.ui.fragments.home.adapters.SearchAdapter
import com.example.taskaty.app.ui.fragments.home.adapters.SearchListener
import com.example.taskaty.app.ui.fragments.home.team.TeamTasksFragment
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.databinding.FragmentSearchBinding
import com.example.taskaty.domain.entities.PersonalTask
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.interactors.SearchInteractor

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate),
	SearchListener, SearchResualtView {
	private val adapter = SearchAdapter(this)
	private lateinit var SearchPresenter: SearchPresenter

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setup()
	}

	private fun setup() {
		SearchPresenter =
			SearchPresenter(SearchInteractor(AllTasksRepositoryImpl.getInstance()), this)
		binding.appbar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
		binding.searchViewResult.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				if (query!!.isEmpty()) {
					changeEmptyState(true)
				} else {
					changeEmptyState(false)
					SearchPresenter.searchTasks(query)
				}
				return true
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				if (newText!!.isEmpty()) {
					changeEmptyState(true)
				} else {
					changeEmptyState(false)
					SearchPresenter.searchTasks(newText)
				}
				return true
			}
		})
	}

	override fun showResults(results: List<Task>) {
		requireActivity().runOnUiThread {
			if (results.isNotEmpty()) {
				changeEmptyState(false)
				binding.recyclerViewSearch.adapter = adapter
				adapter.submitList(results)
			} else {
				changeEmptyState(true)
			}
		}
	}

	private fun changeEmptyState(isVisible: Boolean) {
		(binding.emptySearch).apply {
			binding.emptySearch.emptySearchImage.isVisible = isVisible
			binding.emptySearch.emptySearchText.isVisible = isVisible
			binding.recyclerViewSearch.isVisible = !isVisible
			binding.shadow.isVisible = !isVisible
		}
	}

	override fun showError(message: String) {
		requireActivity().runOnUiThread {
			Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
		}
	}

	override fun onClick(task: Task) {
		if (task is PersonalTask) {
			replaceFragment(TaskDetailsFragment.newInstance(task.id))
		} else {
			replaceFragment(TeamTaskDetailsFragment.getInstance(task.id))
		}
	}

	private fun replaceFragment(fragment: Fragment) {
		val fragmentManager = requireActivity().supportFragmentManager
		val transaction = fragmentManager.beginTransaction()
		transaction.replace(R.id.container_fragment, fragment)
		transaction.addToBackStack(null)
		transaction.commit()
	}


}
