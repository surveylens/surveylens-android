package io.surveylens.surveylens.ui

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.surveylens.surveylens.R
import io.surveylens.surveylens.databinding.ActivityFullScreenQuestionsBinding
import at.autcoding.qualform.model.AnswerItem
import io.surveylens.surveylens.model.Question
import io.surveylens.surveylens.model.QuestionType
import io.surveylens.surveylens.ui.views.MultiSelectAdapter
import io.surveylens.surveylens.ui.views.SingleSelectAdapter

internal class QuestionUIHelper(
    private val binding: ActivityFullScreenQuestionsBinding,
    private val viewModel: SurveyViewModel,
    val context: Context
) {

    fun updateQuestionLayout(question: Question) {
        setContainerVisibility(question.type)
        when (question.type) {
            QuestionType.select, QuestionType.rating -> setQuestionTypeSelectLayout(
                question
            )
            QuestionType.multi_select -> setQuestionTypeMultiSelectLayout(
                question
            )
            QuestionType.yes_no -> setQuestionTypeYesNoLayout(question)
            else -> {
                //TODO
            }
        }
    }

    private fun setQuestionTypeMultiSelectLayout(question: Question) {
        binding.containerMultiSelect.textviewQuestion.text = question.title
        binding.containerMultiSelect.multiSelectAnswers.layoutManager = LinearLayoutManager(context)

        val adapter =
            MultiSelectAdapter(context, ArrayList(question.answers.map { AnswerItem(it, false) }))
        binding.containerMultiSelect.multiSelectAnswers.adapter = adapter
        binding.containerMultiSelect.buttonFinishSelection.setOnClickListener {
            val answers = adapter.selected.map { it.answer.id }

            if (answers.isEmpty()) {
                Toast.makeText(context, R.string.multiselect_question_no_answer, Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.userAnswer(context, answers)
            }
        }
    }

    private fun setQuestionTypeSelectLayout(question: Question) {
        binding.containerSingleSelect.textviewQuestion.text = question.title

        binding.containerSingleSelect.singleSelectAnswers.setLayoutManager(
            LinearLayoutManager(
                context
            )
        )
       // binding.containerSingleSelect.singleSelectAnswers.addItemDecoration(
       //     DividerItemDecoration(
//                context,
//                LinearLayoutManager.VERTICAL
//            )
//        )
        val adapter = SingleSelectAdapter(context, ArrayList(question.answers)).apply {
            onEntryClickListener = {
                viewModel.userAnswer(context, listOf(it.id))
            }
        }

        binding.containerSingleSelect.singleSelectAnswers.setAdapter(adapter)
    }

    private fun setQuestionTypeYesNoLayout(question: Question) {
        binding.containerYesNo.textviewQuestion.text = question.title

        binding.containerYesNo.buttonYes.apply {
            text = question.answers[0].value
            setOnClickListener {
                viewModel.userAnswer(context, listOf(question.answers[0].id))
            }
        }

        binding.containerYesNo.buttonNo.apply {
            text = question.answers[1].value
            setOnClickListener {
                viewModel.userAnswer(context, listOf(question.answers[1].id))
            }
        }
    }

    private fun setContainerVisibility(questionType: QuestionType) {
        when (questionType) {
            QuestionType.select, QuestionType.rating -> {
                binding.containerSingleSelect.container.visibility = View.VISIBLE
                binding.containerYesNo.container.visibility = View.GONE
                binding.containerMultiSelect.container.visibility = View.GONE
            }

            QuestionType.multi_select -> {
                binding.containerMultiSelect.container.visibility = View.VISIBLE
                binding.containerSingleSelect.container.visibility = View.GONE
                binding.containerYesNo.container.visibility = View.GONE
            }

            QuestionType.yes_no -> {
                binding.containerSingleSelect.container.visibility = View.GONE
                binding.containerMultiSelect.container.visibility = View.GONE
                binding.containerYesNo.container.visibility = View.VISIBLE
            }
        }
    }
}