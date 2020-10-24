package io.surveylens.surveylens.model

import java.io.Serializable

internal data class Question(
    val id: Int,
    val title: String,
    val type: QuestionType,
    val answers: List<Answer>
) : Serializable
