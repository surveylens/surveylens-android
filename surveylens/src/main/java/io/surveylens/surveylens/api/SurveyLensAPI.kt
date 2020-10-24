package io.surveylens.surveylens.api

import io.surveylens.surveylens.model.AnswerDAO
import io.surveylens.surveylens.model.BackendResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface SurveyLensAPI {

    companion object {
        const val API = "/public/client/"
    }

    /**
     * @return List of surveys
     */
    @GET("surveys")
    suspend fun getSurveys(): BackendResult

    @GET("devices/{deviceId}/surveys")
    fun getSurveyList(@Path("deviceId") deviceId: String): Call<BackendResult>

    @POST("devices/{deviceId}/surveys/{survey_id}/questions/{question_id}/responses")
    fun postAnswer(
        @Path("deviceId") deviceId: String,
        @Path("survey_id") surveyId: Int,
        @Path("question_id") QuestionId: Int,
        @Body answer: AnswerDAO
    ): Call<ResponseBody>
}
