package com.example.taskaty.app.ui.fragments.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskaty.app.utils.DateTimeUtils
import com.example.taskaty.databinding.ChildRecyclerHomeChartBinding
import com.example.taskaty.databinding.ChildRecyclerHomePersonalDoneBinding
import com.example.taskaty.databinding.ChildRecyclerHomePersonalInprogressBinding
import com.example.taskaty.databinding.ChildRecyclerHomePersonalUpcomingBinding
import com.example.taskaty.domain.entities.PersonalTask
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*

class ParentPersonalAdapter(
    private val InProgress: List<PersonalTask>,
    private val Upcoming: List<PersonalTask>,
    private val Done: List<PersonalTask>,
    private val onViewAllClickListener: OnViewAllClickListener,
    private val onTaskClickListener: OnTaskClickListener

) :
    Adapter<ParentPersonalAdapter.BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            FIRST_ITEM -> {
                val view = ChildRecyclerHomeChartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ChartViewHolder(view.root)
            }

            SECOND_ITEM -> {
                val view = ChildRecyclerHomePersonalInprogressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InProgressViewHolder(view.root)
            }

            THIRD_ITEM -> {
                val view = ChildRecyclerHomePersonalUpcomingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return UpcomingViewHolder(view.root)
            }

            else -> {
                val view = ChildRecyclerHomePersonalDoneBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DoneViewHolder(view.root)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            FIRST_ITEM -> FIRST_ITEM
            SECOND_ITEM -> SECOND_ITEM
            THIRD_ITEM -> THIRD_ITEM
            else -> FOURTH_ITEM
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is ChartViewHolder -> bindChart(holder)
            is InProgressViewHolder -> bindInProgress(holder)
            is UpcomingViewHolder -> bindUpcoming(holder)
            is DoneViewHolder -> bindDone(holder)
        }
    }

    private fun bindChart(holder: ChartViewHolder) {
        val totalTasks = Done.size + InProgress.size + Upcoming.size
        var upComingStatesValue = 0
        var doneStatesValue = 0
        var inProgressStatesValue = 0
        if (totalTasks != 0) {
            upComingStatesValue = (Upcoming.size * 100) / totalTasks
            doneStatesValue = (Done.size * 100) / totalTasks
            inProgressStatesValue = (InProgress.size * 100) / totalTasks
        }
        holder.binding.apply {
            todoStates.text = upComingStatesValue.toString().plus("%")
            doneStates.text = doneStatesValue.toString().plus("%")
            inProgressStates.text = inProgressStatesValue.toString().plus("%")
            chart.isDrawHoleEnabled = true
            chart.setUsePercentValues(false)
            chart.setDrawEntryLabels(false)
            chart.holeRadius = 70f
            chart.centerText = "Total \n$totalTasks"
            chart.setCenterTextSize(11F)
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            val entries = ArrayList<PieEntry>()
            entries.add(PieEntry(Upcoming.size * 1f, "Todo"))
            entries.add(PieEntry(Done.size * 1f, "Done"))
            entries.add(PieEntry(InProgress.size * 1f, "In Progress"))
            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#7FBAA9"))
            colors.add(Color.parseColor("#93CB80"))
            colors.add(Color.parseColor("#418E77"))
            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors
            val data = PieData(dataSet)
            data.setDrawValues(false)
            data.setValueFormatter(PercentFormatter(chart))
            data.setValueTextSize(12f)
            data.setValueTextColor(Color.BLACK)
            chart.data = data
            chart.invalidate()
        }
    }

    private fun bindInProgress(holder: InProgressViewHolder) {
        holder.binding.apply {
            tasksNumber.text = InProgress.size.toString()
            if(InProgress.isNotEmpty()) {
                inProgressViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(1) }
                val adapter = ChildPersonalInProgressAdapter(InProgress, onTaskClickListener)
                childRecycler.adapter = adapter
            }
        }
    }

    private fun bindUpcoming(holder: UpcomingViewHolder) {
        holder.binding.apply {
            tasksNumber.text = Upcoming.size.toString()
            if (Upcoming.isEmpty()) {
                disappearCards(firstCard, secondCard, false)
            } else if (Upcoming.size == 1) {
                upcomingViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(0) }
                val firstItem = Upcoming[FIRST_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = DateTimeUtils.toDateFormat(firstItem.creationTime)
                timeTextFirst.text = DateTimeUtils.toTimeFormat(firstItem.creationTime)
                firstConstraint.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                disappearCards(firstCard, secondCard, true)
            } else {
                upcomingViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(0) }
                val firstItem = Upcoming[FIRST_ITEM]
                val secondItem = Upcoming[SECOND_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = DateTimeUtils.toDateFormat(firstItem.creationTime)
                timeTextFirst.text = DateTimeUtils.toTimeFormat(firstItem.creationTime)
                taskHeaderSecond.text = secondItem.title
                dateTextSecond.text = DateTimeUtils.toDateFormat(secondItem.creationTime)
                timeTextSecond.text = DateTimeUtils.toTimeFormat(secondItem.creationTime)
                firstConstraint.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                secondCard.setOnClickListener { onTaskClickListener.onTaskClick(secondItem) }
            }
        }
    }

    private fun bindDone(holder: DoneViewHolder) {
        holder.binding.apply {
            tasksNumber.text = Done.size.toString()
            if (Done.isEmpty()) {
                disappearCards(cardDoneFirst, cardDoneSecond, false)
            } else if (Done.size == 1) {
                doneViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(2) }
                val firstItem = Done[FIRST_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = DateTimeUtils.toDateFormat(firstItem.creationTime)
                timeTextFirst.text = DateTimeUtils.toTimeFormat(firstItem.creationTime)
                disappearCards(cardDoneFirst, cardDoneSecond, true)
            } else {
                doneViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(2) }
                val firstItem = Done[FIRST_ITEM]
                val secondItem = Done[SECOND_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = DateTimeUtils.toDateFormat(firstItem.creationTime)
                timeTextFirst.text = DateTimeUtils.toTimeFormat(firstItem.creationTime)
                taskHeaderSecond.text = secondItem.title
                dateTextSecond.text = DateTimeUtils.toDateFormat(secondItem.creationTime)
                timeTextSecond.text = DateTimeUtils.toTimeFormat(secondItem.creationTime)
                cardDoneFirst.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                cardDoneSecond.setOnClickListener { onTaskClickListener.onTaskClick(secondItem) }
            }

        }
    }

    private fun disappearCards(firsCard: CardView, secondCard: CardView, isOne: Boolean) {
        if (isOne) {
            secondCard.isVisible = false
            val layoutParam = secondCard.layoutParams
            layoutParam.height = 0
            secondCard.layoutParams = layoutParam
        } else {
            firsCard.isVisible = false
            secondCard.isVisible = false
            val firstCardParam = firsCard.layoutParams
            firstCardParam.height = 0
            firsCard.layoutParams = firstCardParam
            val secondCardParam = secondCard.layoutParams
            secondCardParam.height = 0
            secondCard.layoutParams = secondCardParam
        }
    }

    override fun getItemCount() = FOURTH_ITEM + 1

    abstract class BaseViewHolder(view: View) : ViewHolder(view)

    class InProgressViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomePersonalInprogressBinding.bind(view)
    }

    class UpcomingViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomePersonalUpcomingBinding.bind(view)
    }

    class ChartViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomeChartBinding.bind(view)
    }

    class DoneViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomePersonalDoneBinding.bind(view)
    }

    companion object {
        const val FIRST_ITEM = 0
        const val SECOND_ITEM = 1
        const val THIRD_ITEM = 2
        const val FOURTH_ITEM = 3
    }

}