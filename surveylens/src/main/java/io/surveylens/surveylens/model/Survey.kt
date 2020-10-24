package io.surveylens.surveylens.model

import java.io.Serializable

internal data class Survey(
    val id: Int,
    val publicId: String,
    val shouldSee: Boolean,
    val lifecycleState: LifecycleState,
    val title: String,
    val questions: List<Question>
) : Serializable

enum class LifecycleState {
    initialised, paused, running, archived
}
