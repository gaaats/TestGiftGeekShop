package com.example.testgiftgeekshop.data

import com.example.testgiftgeekshop.BuildConfig
import com.example.testgiftgeekshop.utils.Constances
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofotIstance {

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constances.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api : PromAPI by lazy {
        retrofit.create(PromAPI::class.java)
    }
}

class MyInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}