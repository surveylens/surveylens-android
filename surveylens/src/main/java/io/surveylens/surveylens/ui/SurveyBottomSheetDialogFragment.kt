package io.surveylens.surveylens.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.surveylens.surveylens.databinding.ActivityFullScreenQuestionsBinding
import io.surveylens.surveylens.helper.SurveyService
import io.surveylens.surveylens.model.Survey

internal class SurveyBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var survey: Survey
    private lateinit var binding: ActivityFullScreenQuestionsBinding
    private lateinit var questionUIHelper: QuestionUIHelper

    private val viewModel: SurveyViewModel by viewModels()

    companion object {
        const val EXTRA_RECORDING_ACTION = "recordingAction"

        @JvmStatic
        fun newInstance(surveyId: String): SurveyBottomSheetDialogFragment {
            return SurveyBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_RECORDING_ACTION, surveyId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let { context ->
            survey =
                SurveyService().getSurveyById(
                    context,
                    arguments?.getString(EXTRA_RECORDING_ACTION)!!
                )!!
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = ActivityFullScreenQuestionsBinding.inflate(LayoutInflater.from(context))
        questionUIHelper = QuestionUIHelper(binding, viewModel, requireContext())

        viewModel.surveyState.observe(this) { state ->
            when (state) {
                is SurveyViewModel.SurveyState.Finished -> {
                    //TODO show optional thank you screen
                    dialog?.dismiss()
                }
                is SurveyViewModel.SurveyState.InProgress -> {
                    questionUIHelper.updateQuestionLayout(state.question)
                }
            }
        }

        val survey = SurveyService().getSurveyById(requireContext(), survey.publicId)
        questionUIHelper.updateQuestionLayout(survey!!.questions[0])
        context?.let {
            viewModel.init(it, survey.publicId)
        }


        return binding.root
    }
}