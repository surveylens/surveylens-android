package io.surveylens.surveylens.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.gson.Gson
import io.surveylens.surveylens.model.Survey
import io.surveylens.surveylens.model.UserAnswer
import java.util.*

internal class StorageDB(appContext: Context?) {

    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(appContext)
    private val USER_ANSWERS = "user_answers"
    private val SURVEYS = "survey_list"

    fun putSurveyList(surveyList: List<Survey?>) {
        checkForNullKey(SURVEYS)
        val gson = Gson()
        val objStrings = ArrayList<String>()

        for (obj in surveyList) {
            objStrings.add(gson.toJson(obj))
        }
        putListString(SURVEYS, objStrings)
    }

    fun putUserAnswerList(userAnswerList: List<UserAnswer?>) {
        checkForNullKey(USER_ANSWERS)
        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (obj in userAnswerList) {
            objStrings.add(gson.toJson(obj))
        }
        putListString(USER_ANSWERS, objStrings)
    }

    fun addUserAnswer(userAnswer: UserAnswer?) {
        checkForNullKey(USER_ANSWERS)
        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (obj in userAnswers) {
            objStrings.add(gson.toJson(obj))
        }
        objStrings.add(gson.toJson(userAnswer))
        putListString(USER_ANSWERS, objStrings)
    }

    val surveyList: ArrayList<Survey>
        get() {
            val gson = Gson()
            val objStrings = getListString(SURVEYS)
            val surveys = ArrayList<Survey>()
            for (jObjString in objStrings) {
                val value = gson.fromJson(jObjString, Survey::class.java)
                surveys.add(value)
            }
            return surveys
        }

    val userAnswers: ArrayList<UserAnswer>
        get() {
            val gson = Gson()
            val objStrings = getListString(USER_ANSWERS)
            val answers = ArrayList<UserAnswer>()
            for (jObjString in objStrings) {
                val value = gson.fromJson(jObjString, UserAnswer::class.java)
                answers.add(value)
            }
            return answers
        }

    private fun getListString(key: String): ArrayList<String> {
        return ArrayList(
            listOf(
                *TextUtils.split(
                    preferences.getString(key, ""),
                    "‚‗‚"
                )
            )
        )
    }

    private fun putListString(key: String, stringList: ArrayList<String>) {
        val myStringList = stringList.toTypedArray()
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    private fun checkForNullKey(key: String?) {
        if (key == null) {
            throw NullPointerException()
        }
    }
}