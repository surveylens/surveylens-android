package io.surveylens.surveylens.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import io.surveylens.surveylens.databinding.ActivityFullScreenQuestionsBinding
import io.surveylens.surveylens.helper.SurveyService
import io.surveylens.surveylens.model.Survey

internal class SurveyDialogFragment : DialogFragment() {

    private lateinit var survey: Survey
    private lateinit var binding: ActivityFullScreenQuestionsBinding
    private lateinit var questionUIHelper: QuestionUIHelper

    private val viewModel: SurveyViewModel by viewModels()

    companion object {
        const val EXTRA_RECORDING_ACTION = "recordingAction"

        @JvmStatic
        fun newInstance(surveyId: String): SurveyDialogFragment {
            return SurveyDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_RECORDING_ACTION, surveyId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppCompatDialogStyle)
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityFullScreenQuestionsBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        questionUIHelper = QuestionUIHelper(binding, viewModel, requireContext())

        viewModel.surveyState.observe(this) { state ->
            when (state) {
                is SurveyViewModel.SurveyState.Finished -> {
                    //TODO show optional thank you info
                    dialog?.dismiss()
                }
                is SurveyViewModel.SurveyState.InProgress -> {
                    questionUIHelper.updateQuestionLayout(state.question)
                }
            }
        }
        context?.let {
            viewModel.init(it, survey.publicId)
        }

        return builder.create()
    }
}