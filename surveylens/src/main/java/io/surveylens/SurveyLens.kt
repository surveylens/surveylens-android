package io.surveylens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentManager
import io.surveylens.surveylens.helper.ConfigReader
import io.surveylens.surveylens.helper.SurveyService
import io.surveylens.surveylens.helper.ValidationHelper
import io.surveylens.surveylens.model.LifecycleState
import io.surveylens.surveylens.ui.FullScreenSurveyActivity
import io.surveylens.surveylens.ui.SurveyBottomSheetDialogFragment

object SurveyLens {

    @JvmStatic
    fun launchSurvey(context: Context, surveyId: String) {
        if (!ValidationHelper.validateSetup(context)) {
            return
        }

        val survey = SurveyService().getSurveyById(context, surveyId)
        val isDebug = ConfigReader.isDebug(context)

        if (survey != null) {
            if (survey.shouldSee) {
                if (isDebug) {
                    Log.d("SurveyLens", "Showing survey ${survey.publicId} fullscreen")
                }

                val intent = Intent(context, FullScreenSurveyActivity::class.java)
                intent.putExtra("EXTRA_SURVEY_ID", surveyId)
                context.startActivity(intent)
            } else if (survey.lifecycleState == LifecycleState.initialised && isDebug) {
                Log.d(
                    "SurveyLens",
                    "Normally this survey would not be shown, it is only shown because you run in debug mode"
                )
                Log.d("SurveyLens", "Showing survey ${survey.publicId} fullscreen")

                val intent = Intent(context, FullScreenSurveyActivity::class.java)
                intent.putExtra("EXTRA_SURVEY_ID", surveyId)
                context.startActivity(intent)
            } else if (isDebug) {
                Log.d("SurveyLens", "Survey ${survey.publicId} not shown")
            }

        } else if (isDebug) {
            Log.d("SurveyLens", "Survey $surveyId not available")
        }
    }

    @JvmStatic
    fun launchSurveyAsDialog(
        activity: Activity,
        fragmentManager: FragmentManager,
        surveyId: String
    ) {
        if (!ValidationHelper.validateSetup(activity)) {
            return
        }

        val isDebug = ConfigReader.isDebug(activity)
        val survey = SurveyService().getSurveyById(activity, surveyId)

        if (survey != null) {
            if (survey.shouldSee) {
                if (isDebug) {
                    Log.d("SurveyLens", "Showing survey ${survey.publicId} dialog")
                }

                val dialog = SurveyBottomSheetDialogFragment.newInstance(surveyId)

                dialog.show(fragmentManager, "SurveyLens")
            } else if (survey.lifecycleState == LifecycleState.initialised && isDebug) {
                Log.d(
                    "SurveyLens",
                    "Normally this survey would not be shown, it is only shown because you run in debug mode"
                )
                Log.d("SurveyLens", "Showing survey ${survey.publicId} dialog")

                val dialog = SurveyBottomSheetDialogFragment.newInstance(surveyId)
                dialog.show(fragmentManager, "SurveyLens")
            } else if (isDebug) {
                Log.d("SurveyLens", "Survey ${survey.publicId} not shown")
            }
        } else if (isDebug) {
            Log.d("SurveyLens", "Survey $surveyId not available.")
        }
    }

    @JvmStatic
    fun initialize(context: Context) {
        if (!ValidationHelper.validateSetup(context)) {
            return
        }

        SurveyService().initialize(context)
    }

    @JvmStatic
    fun getPublicSurveyIds(context: Context): List<String> =
        SurveyService().getPublicSurveyIds(context)
}