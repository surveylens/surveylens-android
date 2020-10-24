package io.surveylens.surveylens.helper

import android.content.Context
import io.surveylens.surveylens.model.Survey
import io.surveylens.surveylens.model.UserAnswer
import java.util.*
import kotlin.collections.ArrayList

internal class Storage {
    internal fun updateLocalSurveys(context: Context, surveys: List<Survey>) {
        StorageDB(context).putSurveyList(surveys)
    }

    internal fun getSurveyById(context: Context, surveyId: String): Survey? {
        val surveys: ArrayList<Survey> = StorageDB(context).surveyList
        return surveys.find { it.publicId == surveyId }
    }

    internal fun getPublicSurveyIds(context: Context): List<String> {
        val surveys: ArrayList<Survey> = StorageDB(context).surveyList
        return surveys.filter { it.publicId.isNotEmpty() }.map { it.publicId }
    }

    internal fun storeUserAnswer(context: Context, userAnswer: UserAnswer) {
        StorageDB(context).addUserAnswer(userAnswer)
    }

    internal fun updateUserAnswers(context: Context, userAnswers: List<UserAnswer>) {
        StorageDB(context).putUserAnswerList(userAnswers)
    }

    internal fun getUserAnswers(context: Context): ArrayList<UserAnswer> {
        return StorageDB(context).userAnswers
    }

    internal fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences("qualform", Context.MODE_PRIVATE)
        var deviceId = prefs.getString("deviceId", null)

        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString("deviceId", deviceId).apply()
        }

        return deviceId
    }
}