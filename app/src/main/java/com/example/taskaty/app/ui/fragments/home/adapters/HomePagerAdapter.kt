package com.example.taskaty.app.ui.fragments.home.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskaty.app.ui.fragments.home.personal.PersonalTasksFragment
import com.example.taskaty.app.ui.fragments.home.team.TeamTasksFragment


class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) PersonalTasksFragment() else TeamTasksFragment()
    }
}
