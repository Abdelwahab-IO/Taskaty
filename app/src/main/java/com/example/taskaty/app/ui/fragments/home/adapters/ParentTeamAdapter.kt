package com.example.taskaty.app.ui.fragments.home.adapters

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskaty.databinding.ChildRecyclerHomeChartBinding
import com.example.taskaty.databinding.ChildRecyclerHomeTeamDoneBinding
import com.example.taskaty.databinding.ChildRecyclerHomeTeamInprogressBinding
import com.example.taskaty.databinding.ChildRecyclerHomeTeamUpcomingBinding
import com.example.taskaty.domain.entities.TeamTask
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*

class ParentTeamAdapter(
    private val InProgress: List<TeamTask>,
    private val Upcoming: List<TeamTask>,
    private val Done: List<TeamTask>,
    private val onViewAllClickListener: OnViewAllClickListener,
    private val onTaskClickListener: OnTaskClickListener
) : Adapter<ParentTeamAdapter.BaseViewHolder>() {

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
                val view = ChildRecyclerHomeTeamInprogressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InProgressViewHolder(view.root)
            }

            THIRD_ITEM -> {
                val view = ChildRecyclerHomeTeamUpcomingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return UpcomingViewHolder(view.root)
            }

            else -> {
                val view = ChildRecyclerHomeTeamDoneBinding.inflate(
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
            todoStates.text = "$upComingStatesValue %"
            doneStates.text = "$doneStatesValue %"
            inProgressStates.text = "$inProgressStatesValue %"
            chart.isDrawHoleEnabled = true
            chart.setUsePercentValues(false)
            chart.setDrawEntryLabels(false)
            chart.holeRadius = 70f
            chart.centerText = "Total \n$totalTasks"
            chart.setCenterTextSize(11F)
            chart.description.isEnabled = false
            chart.legend.isEnabled = false
            chartTitle.text = "Team Todo States"
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
            if(InProgress.isNotEmpty()){
                val adapter = ChildTeamInProgressAdapter(InProgress, onTaskClickListener)
                inProgressViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(1) }
                childRecycler.adapter = adapter
            }
        }
    }

    private fun bindUpcoming(holder: UpcomingViewHolder) {
        holder.binding.apply {
            tasksNumber.text = Upcoming.size.toString()
            if (Upcoming.isEmpty()) {
                disappearCards(upcomingFirstCard, upcomingSecondCard, false)
            } else if (Upcoming.size == 1) {
                upcomingViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(0) }
                val firstItem = Upcoming[FIRST_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = formatDate(firstItem.creationTime, true)
                timeTextFirst.text = formatDate(firstItem.creationTime, false)
                upcomingFirstCard.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                disappearCards(upcomingFirstCard, upcomingSecondCard, true)
            } else {
                upcomingViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(0) }
                val firstItem = Upcoming[FIRST_ITEM]
                val secondItem = Upcoming[SECOND_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = formatDate(firstItem.creationTime, true)
                timeTextFirst.text = formatDate(firstItem.creationTime, false)
                taskHeaderSecond.text = secondItem.title
                dateTextSecond.text = formatDate(secondItem.creationTime, true)
                timeTextSecond.text = formatDate(secondItem.creationTime, false)
                upcomingFirstCard.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                upcomingSecondCard.setOnClickListener {onTaskClickListener.onTaskClick(secondItem) }
            }
        }
    }

    private fun bindDone(holder: DoneViewHolder) {
        holder.binding.apply {
            tasksNumber.text = Done.size.toString()
            if (Done.isEmpty()) {
                disappearCards(firstCard, secondCard, false)
            } else if (Done.size == 1) {
                doneViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(2) }
                val firstItem = Done[FIRST_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = formatDate(firstItem.creationTime, true)
                timeTextFirst.text = formatDate(firstItem.creationTime, false)
                firstCard.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                disappearCards(firstCard, secondCard, true)
            } else {
                doneViewAll.setOnClickListener { onViewAllClickListener.onViewAllClick(2) }
                val firstItem = Done[FIRST_ITEM]
                val secondItem = Done[SECOND_ITEM]
                taskHeaderFirst.text = firstItem.title
                dateTextFirst.text = formatDate(firstItem.creationTime, true)
                timeTextFirst.text = formatDate(firstItem.creationTime, false)
                taskHeaderSecond.text = secondItem.title
                dateTextSecond.text = formatDate(secondItem.creationTime, true)
                timeTextSecond.text = formatDate(secondItem.creationTime, false)
                firstCard.setOnClickListener { onTaskClickListener.onTaskClick(firstItem) }
                secondCard.setOnClickListener { onTaskClickListener.onTaskClick(secondItem) }
            }
        }
    }

    private fun formatDate(
        creationTime: String,
        isDate: Boolean
    ): String {
        val date: String
        val inputDateFormat =
            SimpleDateFormat(INPUT_DATE_PATTERN, Locale.getDefault())
        val outputDateFormat =
            SimpleDateFormat(OUTPUT_DATE_PATTERN, Locale.getDefault())
        val outputTimeFormat =
            SimpleDateFormat(OUTPUT_TIME_PATTERN, Locale.getDefault())
        date = if (isDate) {
            outputDateFormat.format(inputDateFormat.parse(creationTime))
        } else {
            outputTimeFormat.format(inputDateFormat.parse(creationTime))
        }
        return date
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
        val binding = ChildRecyclerHomeTeamInprogressBinding.bind(view)
    }

    class UpcomingViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomeTeamUpcomingBinding.bind(view)
    }

    class ChartViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomeChartBinding.bind(view)
    }

    class DoneViewHolder(view: View) : BaseViewHolder(view) {
        val binding = ChildRecyclerHomeTeamDoneBinding.bind(view)
    }

    companion object {
        const val FIRST_ITEM = 0
        const val SECOND_ITEM = 1
        const val THIRD_ITEM = 2
        const val FOURTH_ITEM = 3
        const val INPUT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        const val OUTPUT_DATE_PATTERN = "yyyy-MM-dd"
        const val OUTPUT_TIME_PATTERN = "HH:mm"
    }

}