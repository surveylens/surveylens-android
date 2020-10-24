package io.surveylens.surveylens.api

import android.content.Context
import io.surveylens.surveylens.helper.ConfigReader
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

internal class SurveyLensApiBuilder(
    private val defaultHost: String = "https://api.surveylens.io",
    private val connectionTimeout: Long = 60L
) {

    fun getSurveyLensApi(context: Context): SurveyLensAPI {
        val host = ConfigReader.getApiUrl(context) ?: defaultHost
        val token = ConfigReader.getApiKey(context)
            ?: throw Exception("SurveyLens API token not provided!")

        return Retrofit.Builder()
            .baseUrl(host + SurveyLensAPI.API)
            .addConverterFactory(getGsonFactory())
            .client(getHttpClient(token).build())
            .build()
            .create(SurveyLensAPI::class.java)
    }

    fun getGsonFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder().registerTypeAdapter(
                Date::class.java,
                DateAdapter()
            ).setLenient().create()
        )
    }

    private fun getHttpClient(bearerToken: String): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(connectionTimeout, TimeUnit.SECONDS)
            .addInterceptor(AuthenticationInterceptor(bearerToken))
    }
}
