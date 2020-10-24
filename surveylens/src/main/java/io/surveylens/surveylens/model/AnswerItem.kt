package at.autcoding.qualform.model

import io.surveylens.surveylens.model.Answer
import java.io.Serializable

internal data class AnswerItem(
    val answer: Answer,
    var checked: Boolean
) : Serializable