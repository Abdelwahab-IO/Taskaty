package com.example.taskaty.app.ui.fragments.viewAll.team

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskaty.R
import com.example.taskaty.app.ui.fragments.home.adapters.OnTaskClickListener
import com.example.taskaty.databinding.ItemTeamViewAllBinding


import com.example.taskaty.domain.entities.TeamTask
import com.example.taskaty.app.utils.DateTimeUtils

class ViewAllTeamTasksAdapter(private val onTaskClickListener: OnTaskClickListener) :
    ListAdapter<TeamTask, ViewAllTeamTasksAdapter.ViewAllHolder>(TeamTaskDiffCallback()) {


    class ViewAllHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemTeamViewAllBinding.bind(itemView)
    }

    class TeamTaskDiffCallback : DiffUtil.ItemCallback<TeamTask>() {
        override fun areItemsTheSame(oldItem: TeamTask, newItem: TeamTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TeamTask, newItem: TeamTask): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_team_view_all, parent, false)
        return ViewAllHolder(view)
    }


    override fun onBindViewHolder(holder: ViewAllHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            root.setOnClickListener { onTaskClickListener.onTaskClick(item) }
            textTitle.text = item.title
            textContent.text = item.description
            textState.text = getStatusNames(item.status)
            textState.backgroundTintList = ContextCompat.getColorStateList(
                holder.itemView.context, getStatusColors(item.status)
            )
            textAssignee.text = item.assignee.split(",").size.toString()

            textAssignee.backgroundTintList = ContextCompat.getColorStateList(
                holder.itemView.context, getStatusColors(item.status)
            )
            val inputDateString = item.creationTime
            textCalender.text = DateTimeUtils.toDateFormat(inputDateString)
            textClock.text = DateTimeUtils.toTimeFormat(inputDateString)
        }

    }

    private fun getStatusNames(status: Int?): String {
        return when (status) {
            0 -> "ToDo"
            1 -> "In Progress"
            else -> "Done"
        }
    }

    private fun getStatusColors(status: Int?): Int {
        return when (status) {
            0 -> R.color.todo_color
            1 -> R.color.in_progress_color
            else -> R.color.done_color
        }
    }


}