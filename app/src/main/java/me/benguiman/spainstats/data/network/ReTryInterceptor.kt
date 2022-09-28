package me.benguiman.spainstats.data.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ReTryInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var response = chain.proceed(chain.request())
        var tryCount = 0
        while (!response.isSuccessful && tryCount < 3) {
            tryCount++
            response.close()
            response = chain.call().clone().execute()
        }

        return response
    }
}