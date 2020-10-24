package io.surveylens.surveylens.api

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthenticationInterceptor(
    private val bearerToken: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().run {
            addHeader("Authorization", "Bearer $bearerToken")
            build()
        }

        return chain.proceed(request)
    }
}
