package me.benguiman.spainstats.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ReTryInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var response: Response? = kotlin.runCatching { chain.proceed(chain.request()) }
            .getOrNull()
        var tryCount = 0
        while (response?.isSuccessful != true && tryCount < 3) {
            tryCount++
            response?.close()
            try {
                response = chain.proceed(chain.call().clone().request())
            } catch (e: Exception) {
                if (tryCount >= 3) {
                    throw e
                }
            }
        }

        return response ?: throw IllegalStateException("Error retrieving network response")
    }
}