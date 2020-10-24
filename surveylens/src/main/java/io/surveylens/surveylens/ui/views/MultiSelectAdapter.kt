package io.surveylens.surveylens.ui.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import at.autcoding.qualform.model.AnswerItem
import io.surveylens.surveylens.R
import io.surveylens.surveylens.ui.views.MultiSelectAdapter.MultiViewHolder
import java.util.*

internal class MultiSelectAdapter(
    private val context: Context,
    var all: ArrayList<AnswerItem>
) : RecyclerView.Adapter<MultiViewHolder>() {
    fun setAnswers(answers: ArrayList<AnswerItem>) {
        all = ArrayList()
        all = answers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MultiViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_answer_multiselect, viewGroup, false)
        return MultiViewHolder(view)
    }

    override fun onBindViewHolder(
        multiViewHolder: MultiViewHolder,
        position: Int
    ) {
        multiViewHolder.bind(all[position])
    }

    override fun getItemCount(): Int {
        return all.size
    }

    internal inner class MultiViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val button: Button
        private val itemBackgroundLayout: LinearLayout
        fun bind(answerItem: AnswerItem) {

            adaptColors(answerItem, itemBackgroundLayout, button)

            button.text = answerItem.answer.value
            button.setOnClickListener {
                answerItem.checked = !answerItem.checked
                adaptColors(answerItem, itemBackgroundLayout, button)
            }
        }

        init {
            button = itemView.findViewById(R.id.button)
            itemBackgroundLayout = itemView.findViewById(R.id.listItemBackground)
        }
    }

    fun adaptColors(
        answerItem: AnswerItem,
        itemBackgroundLayout: LinearLayout,
        button: Button
    ) {
        button.setBackgroundColor(
            if (answerItem.checked) {
                ContextCompat.getColor(context, R.color.listItemChecked)
            } else {
                ContextCompat.getColor(context, android.R.color.transparent)
            }
        )

        button.setTextColor(
            if (answerItem.checked) {
                ContextCompat.getColor(context, R.color.listItemCheckedForeground)
            } else {
                ContextCompat.getColor(context, R.color.listItemUncheckedForeground)
            }
        )
    }

    val selected: ArrayList<AnswerItem>
        get() {
            val selected = ArrayList<AnswerItem>()
            for (i in all.indices) {
                if (all[i].checked) {
                    selected.add(all[i])
                }
            }
            return selected
        }

}