package io.surveylens.surveylens.helper

import android.content.Context
import android.util.Log

internal object ValidationHelper {
    private const val TAG = "ValidationHelper"

    fun validateSetup(context: Context): Boolean {
        val configValidation = ConfigReader.validateConfig(context)

        if(configValidation == ConfigValidation.NO_CONFIG_FILE) {
            Log.e(TAG, "No config file found! Pls check if you put your config file in the right directory!")
            return false
        } else if(configValidation == ConfigValidation.NO_API_KEY) {
            Log.e(TAG, "There is no API key defined in your config file. Pls generate an API key in the web interface and put it into your config file!")
            return false
        }

        return true
    }

    fun validateConfig(context: Context): Boolean {
        val configValidation = ConfigReader.validateConfig(context)

        if(configValidation == ConfigValidation.NO_CONFIG_FILE) {
            Log.e(TAG, "No config file found! Pls check if you put your config file in the right directory!")
            return false
        } else if(configValidation == ConfigValidation.NO_API_KEY) {
            Log.e(TAG, "There is no API key defined in your config file. Pls generate an API key in the web interface and put it into your config file!")
            return false
        }

        return true
    }
}

internal enum class ConfigValidation {
    EVERYTHING_OK, NO_CONFIG_FILE, NO_API_KEY
}