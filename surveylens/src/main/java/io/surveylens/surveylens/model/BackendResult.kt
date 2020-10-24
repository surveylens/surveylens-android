package io.surveylens.surveylens.model

import java.io.Serializable

internal data class BackendResult(
    val surveys: List<Survey>
) : Serializable
