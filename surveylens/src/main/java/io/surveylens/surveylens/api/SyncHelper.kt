package io.surveylens.surveylens.api

import android.content.Context
import android.util.Log
import io.surveylens.surveylens.helper.ConfigReader
import io.surveylens.surveylens.helper.Storage
import io.surveylens.surveylens.model.AnswerDAO
import java.net.UnknownHostException
import java.util.*

internal class SyncHelper(val context: Context) {

    private val isDebug = ConfigReader.isDebug(context)

    enum class SyncResult {
        SUCCESSFUL,
        UNAUTHORIZED,
        ERROR
    }

    fun updateLocalSurveys(): SyncResult {
        try {
            val request = SurveyLensApiBuilder().getSurveyLensApi(context)
                .getSurveyList(Storage().getDeviceId(context)).execute()

            when {
                request.isSuccessful -> {
                    val surveysFromServer = request.body()
                    surveysFromServer?.surveys?.let {
                        Storage().updateLocalSurveys(context, it)
                    }

                    if (isDebug) {
                        Log.d(
                            "SurveyLens",
                            "Successfully synchronized ${surveysFromServer?.surveys?.size} surveys"
                        )
                    }

                    return SyncResult.SUCCESSFUL
                }
                request.code() == 401 -> {
                    if (isDebug) {
                        Log.d(
                            "SurveyLens",
                            "SurveyLens authentication error. Did you provide a valid API token?"
                        )
                    }
                    return SyncResult.UNAUTHORIZED
                }
                else -> {
                    if (isDebug) {
                        Log.d(
                            "SurveyLens",
                            "Error while synchronizing. Got result code ${request.code()}"
                        )
                    }
                    return SyncResult.ERROR
                }
            }

        } catch (e: UnknownHostException) {
            if (isDebug) {
                Log.d("SurveyLens", "Could not upload Answers: unknown host")
            }

            return SyncResult.ERROR
        } catch (e: Exception) {
            if (isDebug) {
                Log.d("SurveyLens", "Could not upload Answers: ${e.message}")
            }
            return SyncResult.ERROR
        }
    }

    fun uploadAnswers(): SyncResult {
        var syncResult = SyncResult.SUCCESSFUL

        try {
            val userAnswers = Storage().getUserAnswers(context)
            loop@ while (userAnswers.isNotEmpty()) {
                val answer = userAnswers.first()
                val answerDAO = AnswerDAO(answer.answerIds)
                val request = SurveyLensApiBuilder().getSurveyLensApi(context).postAnswer(
                    getDeviceId(context),
                    answer.surveyId,
                    answer.questionId,
                    answerDAO
                )

                val result = request.execute()
                when {
                    result.isSuccessful -> {
                        if (isDebug) {
                            Log.d(
                                "SurveyLens",
                                "Successfully uploaded answer."
                            )
                        }

                        userAnswers.remove(answer)
                        Storage().updateUserAnswers(context, userAnswers)
                    }
                    result.code() == 400 || result.code() in 402..499 -> {
                        if (isDebug) {
                            Log.d(
                                "SurveyLens",
                                "Answer not accepted by server: $answer got result code ${result.code()}: ${result.errorBody()?.string()}"
                            )
                        }

                        userAnswers.remove(answer)
                        Storage().updateUserAnswers(context, userAnswers)
                    }
                    result.code() == 401 -> {
                        syncResult = SyncResult.UNAUTHORIZED
                        if (isDebug) {
                            Log.d(
                                "SurveyLens",
                                "SurveyLens authentication error. Did you provide a valid API token?"
                            )
                        }
                        break@loop
                    }
                    else -> {
                        if (isDebug) {
                            Log.d(
                                "SurveyLens",
                                "Error uploading answer: $answer got result code ${result.code()}"
                            )
                        }
                        syncResult = SyncResult.ERROR
                        break@loop
                    }
                }
            }

            return syncResult
        } catch (e: UnknownHostException) {
            if (isDebug) {
                Log.d("SurveyLens", "Could not upload Answers: unknown host")
            }

            return SyncResult.ERROR
        } catch (e: Exception) {
            if (isDebug) {
                Log.d("SurveyLens", "Could not upload Answers: ${e.message}")
            }
            return SyncResult.ERROR
        }
    }


    private fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences("qualform", Context.MODE_PRIVATE)

        var deviceId = prefs.getString("deviceId", null)

        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            prefs.edit().putString("deviceId", deviceId).apply()
        }
        return deviceId
    }
}