package io.surveylens.surveylens.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

internal class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val successfulUpload = SyncHelper(applicationContext).uploadAnswers()
        val successfulUpdate = SyncHelper(applicationContext).updateLocalSurveys()

        return if (successfulUpload == SyncHelper.SyncResult.SUCCESSFUL && successfulUpdate == SyncHelper.SyncResult.SUCCESSFUL) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}