package io.surveylens.surveylens.helper

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import io.surveylens.surveylens.api.SyncWorker
import io.surveylens.surveylens.model.Survey
import io.surveylens.surveylens.model.UserAnswer

internal class SurveyService {

    private lateinit var applicationContext: Context
    private val lifecycleListener: ApplicationLifecycleListener by lazy {
        ApplicationLifecycleListener(applicationContext)
    }

    fun getSurveyById(context: Context, surveyId: String): Survey? =
        Storage().getSurveyById(context, surveyId)

    fun getPublicSurveyIds(context: Context): List<String> = Storage().getPublicSurveyIds(context)

    fun initialize(context: Context) {
        this.applicationContext = context.applicationContext
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleListener)
    }

    fun storeAnswer(context: Context, surveyId: Int, questionId: Int, answerIds: List<Int>) {
        val answer = UserAnswer(
            answerIds = answerIds,
            surveyId = surveyId,
            questionId = questionId
        )

        Storage().storeUserAnswer(context.applicationContext, answer)
        syncData(context.applicationContext)
    }

    fun syncData(context: Context) {
        val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(context).enqueue(uploadWorkRequest)
    }
}