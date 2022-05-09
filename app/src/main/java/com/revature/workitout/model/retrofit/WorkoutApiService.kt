package com.revature.workitout.model.retrofit

import com.revature.workitout.BuildConfig
import com.revature.workitout.model.retrofit.allexcercises.Exercise
import retrofit2.http.GET
import retrofit2.http.Headers

interface WorkoutApiService {


    @Headers(
        "X-RapidAPI-Host: exercisedb.p.rapidapi.com",
        "X-RapidAPI-Key: ${BuildConfig.API_KEY}"
    )
    @GET("exercises")
    suspend fun getAllExercises(): List<Exercise>

}