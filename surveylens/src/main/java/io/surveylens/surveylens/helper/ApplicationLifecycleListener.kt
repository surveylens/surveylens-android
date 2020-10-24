package io.surveylens.surveylens.helper

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.surveylens.surveylens.api.SyncHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ApplicationLifecycleListener(private val applicationContext: Context) : LifecycleObserver {

    // old implementation, initially fetch the data - maybe still needed in the future if the workmanager is to slow?
    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onApplicationResume() {
        scope.launch(Dispatchers.IO) {
            val successfulUpload = SyncHelper(applicationContext).uploadAnswers()
            val successfulUpdate = SyncHelper(applicationContext).updateLocalSurveys()
        }
        // SurveyService().syncData(applicationContext)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
    }
}