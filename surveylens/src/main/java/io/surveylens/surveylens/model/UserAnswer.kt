package io.surveylens.surveylens.model

import java.io.Serializable

internal data class UserAnswer(
    val answerIds: List<Int>,
    val surveyId: Int,
    val questionId: Int
) : Serializable