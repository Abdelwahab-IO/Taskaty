package com.example.taskaty.app.ui.fragments.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskaty.app.utils.DateTimeUtils
import com.example.taskaty.databinding.ItemInprogressTeamCardBinding
import com.example.taskaty.domain.entities.TeamTask

class ChildTeamInProgressAdapter(
    private val items: List<TeamTask>,
    private val onTaskClickListener: OnTaskClickListener
) :
    Adapter<ChildTeamInProgressAdapter.InProgressViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InProgressViewHolder {
        val binding = ItemInprogressTeamCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InProgressViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: InProgressViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            root.setOnClickListener { onTaskClickListener.onTaskClick(item) }
            timeTextTeam.text = item.creationTime
            taskCardHeaderInProgressTeam.text = item.title
            taskDetailsTeam.text = item.description
            dateTextTeam.text = DateTimeUtils.toDateFormat(item.creationTime)
            timeTextTeam.text = DateTimeUtils.toTimeFormat(item.creationTime)
            assigneeCountCircle.text = item.assignee.split(",").filter { it != "" }.size.toString()
        }
    }

    class InProgressViewHolder(val binding: ItemInprogressTeamCardBinding) :
        ViewHolder(binding.root)
}