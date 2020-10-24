package io.surveylens.surveylens.ui.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import io.surveylens.surveylens.R
import io.surveylens.surveylens.model.Answer
import io.surveylens.surveylens.ui.views.SingleSelectAdapter.SingleViewHolder

internal class SingleSelectAdapter(
    private val context: Context,
    var answers: List<Answer>
) : RecyclerView.Adapter<SingleViewHolder>() {

    var onEntryClickListener: ((answer: Answer) -> Unit)? = null

    fun setAllAnswers(answers: List<Answer>) {
        this.answers = answers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SingleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.list_item_answer_multiselect, viewGroup, false)
        return SingleViewHolder(view, onEntryClickListener)
    }

    override fun onBindViewHolder(
        singleViewHolder: SingleViewHolder,
        position: Int
    ) {
        singleViewHolder.bind(answers[position])
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class SingleViewHolder(
        itemView: View,
        private val onEntryClickListener: ((answer: Answer) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val button: Button

        fun bind(answer: Answer) {
            button.text = answer.value
            button.setOnClickListener {
                this.onEntryClickListener?.invoke(answer)
            }
        }

        init {
            button = itemView.findViewById(R.id.button)
        }
    }

}