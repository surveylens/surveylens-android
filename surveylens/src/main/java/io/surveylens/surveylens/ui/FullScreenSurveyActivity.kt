package io.surveylens.surveylens.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import io.surveylens.surveylens.databinding.ActivityFullScreenQuestionsBinding

internal class FullScreenSurveyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenQuestionsBinding
    private lateinit var questionUIHelper: QuestionUIHelper

    private val viewModel: SurveyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullScreenQuestionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        questionUIHelper = QuestionUIHelper(binding, viewModel, this)

        viewModel.surveyState.observe(this) { state ->
            when (state) {
                //TODO we have to remove the toast. maybe add something else? optional?
                is SurveyViewModel.SurveyState.Finished -> {
                    finish()
                }
                is SurveyViewModel.SurveyState.InProgress -> {
                    questionUIHelper.updateQuestionLayout(state.question)
                }
            }
        }

        intent.getStringExtra("EXTRA_SURVEY_ID")?.let {
            viewModel.init(applicationContext, it)
        }
    }
}