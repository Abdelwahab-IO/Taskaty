package com.example.taskaty.app.ui.fragments.home

import android.os.Bundle
import android.view.View
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.SearchFragment
import com.example.taskaty.app.ui.fragments.abstractFragments.BaseFragment
import com.example.taskaty.app.ui.fragments.home.adapters.HomePagerAdapter
import com.example.taskaty.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val fragmentList by lazy {
        listOf(
            PersonalTasksFragment(), TeamTasksFragment()
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = HomePagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.personal)
                else -> getString(R.string.team)
            }
        }.attach()

        binding.newTaskFAB.setOnClickListener {
            val bottomSheet = NewTaskBottomSheetFragment(binding.tabLayout.selectedTabPosition)
            bottomSheet.show(childFragmentManager, "tag")
        }
       binding.searchViewHome.setOnQueryTextFocusChangeListener { _, _ ->
              requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_fragment, SearchFragment())
                .addToBackStack(null)
                .commit()
       }
    }
}