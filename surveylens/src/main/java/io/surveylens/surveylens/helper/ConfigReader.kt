package io.surveylens.surveylens.helper

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.Log
import java.io.IOException
import java.util.*

internal object ConfigReader {
    private const val TAG = "ConfigReader"

    fun getApiKey(context: Context) = getConfigValue(context, "api_key")
    fun getApiUrl(context: Context) = getConfigValue(context, "api_url")
    fun isDebug(context: Context) = getConfigValue(context, "debug") == "true"

    fun validateConfig(context: Context): ConfigValidation {
        return try {
            val apiKey = readConfigValue(context, "api_key")
            if (apiKey.isNullOrBlank()) {
                ConfigValidation.NO_API_KEY
            } else {
                ConfigValidation.EVERYTHING_OK
            }
        } catch (e: NotFoundException) {
            ConfigValidation.NO_CONFIG_FILE
        } catch (e: IOException) {
            ConfigValidation.NO_CONFIG_FILE
        }
    }

    private fun getConfigValue(context: Context, name: String?): String? {
        try {
            return readConfigValue(context, name)
        } catch (e: NotFoundException) {
            Log.e(
                TAG,
                "Unable to find the config file: " + e.message
            )
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Failed to open config file."
            )
        }
        return null
    }

    private fun readConfigValue(context: Context, name: String?): String? {
        val resources = context.resources
        val assetManager = resources.assets
        val inputStream = assetManager.open("surveylens.properties")
        val properties = Properties()
        properties.load(inputStream)
        inputStream.close()
        return properties.getProperty(name)
    }
}