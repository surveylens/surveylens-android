package io.surveylens.surveylens.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.surveylens.surveylens.helper.SurveyService
import io.surveylens.surveylens.model.Question
import io.surveylens.surveylens.model.Survey

internal class SurveyViewModel : ViewModel() {

    private val surveyService = SurveyService()
    private var questionIndex = 0
    private lateinit var survey: Survey

    private val surveyStateInternal = MutableLiveData<SurveyState>()
    val surveyState: LiveData<SurveyState> = surveyStateInternal

    fun init(context: Context, surveyId: String) {
        SurveyService().getSurveyById(context, surveyId)?.let {
            survey = it
        }

        questionIndex = 0
        surveyStateInternal.postValue(SurveyState.InProgress(survey.questions[questionIndex]))
    }

    fun userAnswer(context: Context, answerIds: List<Int>) {
        surveyService.storeAnswer(context, survey.id, survey.questions[questionIndex].id, answerIds)

        if (questionIndex < survey.questions.size - 1) {
            questionIndex++
            survey.questions[questionIndex].let { question ->
                surveyStateInternal.postValue(SurveyState.InProgress(question))
            }
        } else {
            surveyStateInternal.postValue(SurveyState.Finished)
        }
    }



    sealed class SurveyState {
        data class InProgress(val question: Question) : SurveyState()
        object Finished : SurveyState()
    }
}