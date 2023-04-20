package com.example.taskaty.app.ui.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.auth.login.LoginFragment
import com.example.taskaty.app.ui.fragments.home.adapters.HomePagerAdapter
import com.example.taskaty.app.ui.fragments.home.bottomSheet.NewTaskBottomSheetFragment
import com.example.taskaty.app.ui.fragments.search.SearchFragment
import com.example.taskaty.data.repositories.AllTasksRepositoryImpl
import com.example.taskaty.data.repositories.AuthRepositoryImpl
import com.example.taskaty.data.response.RepoCallback
import com.example.taskaty.data.response.RepoResponse
import com.example.taskaty.databinding.FragmentHomeBinding
import com.example.taskaty.domain.entities.Task
import com.example.taskaty.domain.interactors.AuthInteractor
import com.example.taskaty.domain.interactors.SizeInteractor
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), HomeView {
    private val taskSize = SizeInteractor(AllTasksRepositoryImpl.getInstance())
    val presenter = HomePresenter(this, taskSize, AuthInteractor(AuthRepositoryImpl.getInstance()))
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        presenter.loadActiveTasksCount()
        binding.searchViewHome.setOnClickListener {
            replaceFragment(SearchFragment())
        }
        binding.buttonLogout.setOnClickListener {
            presenter.onlogoutClicked()
        }
        binding.viewPager.adapter = HomePagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.personal)
                else -> getString(R.string.team)
            }
        }.attach()

        binding.newTaskFAB.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val bottomSheet =
            NewTaskBottomSheetFragment.newInstance(binding.tabLayout.selectedTabPosition)
        bottomSheet.show(childFragmentManager, "tag")
    }

    override fun showActiveTasksCount(count: Int) {
        requireActivity().runOnUiThread {
            binding.ActiveTasksCount.text = count.toString().plus(" Active Tasks")
        }
    }

    override fun showError(errorMessage: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            binding.imageError.visibility = View.VISIBLE
        }
    }

    override fun navigateToLogin() {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, LoginFragment())
        transaction.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}