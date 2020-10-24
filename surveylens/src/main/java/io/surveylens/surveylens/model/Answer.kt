package io.surveylens.surveylens.model

import java.io.Serializable

internal data class Answer(
    val id: Int,
    val value: String
) : Serializable
