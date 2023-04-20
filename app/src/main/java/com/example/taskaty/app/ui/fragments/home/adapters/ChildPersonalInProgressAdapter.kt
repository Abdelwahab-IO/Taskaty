package com.example.taskaty.app.ui.fragments.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskaty.app.utils.DateTimeUtils
import com.example.taskaty.databinding.ItemInprogressPersonalCardBinding
import com.example.taskaty.domain.entities.PersonalTask

class ChildPersonalInProgressAdapter(
    private val items: List<PersonalTask>,
    private val onTaskClickListener: OnTaskClickListener
) :
    Adapter<ChildPersonalInProgressAdapter.InProgressViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InProgressViewHolder {
        val binding = ItemInprogressPersonalCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return InProgressViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: InProgressViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            root.setOnClickListener { onTaskClickListener.onTaskClick(item) }
            taskCardHeaderInProgress.text = item.title
            taskCardDescriptionInProgress.text = item.description
            taskCardDateInProgress.text = DateTimeUtils.toDateFormat(item.creationTime)
            timeTextInProgress.text = DateTimeUtils.toTimeFormat(item.creationTime)
        }
    }

    class InProgressViewHolder(val binding: ItemInprogressPersonalCardBinding) :
        ViewHolder(binding.root) {
    }
}