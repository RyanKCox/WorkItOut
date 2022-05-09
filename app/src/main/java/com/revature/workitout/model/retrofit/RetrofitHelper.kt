package com.revature.workitout.model.retrofit

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitHelper {


    private val retrofit: Retrofit
    const val BASE_URL = "https://exercisedb.p.rapidapi.com/"


    init{
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(0, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor).build()

        retrofit = builder.client(okHttpClient).build()

        Log.d("RetrofitHelper","Retrofit Set Up")
    }
    fun getWorkoutService():WorkoutApiService {
        Log.d("RetrofitHelper","Retrofit get Service")
        return retrofit.create(WorkoutApiService::class.java)
    }
}