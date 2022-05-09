package com.revature.workitout.model.retrofit

import com.revature.workitout.model.retrofit.allexcercises.Exercise
import retrofit2.http.GET
import retrofit2.http.Headers

interface WorkoutApiService {

    @Headers(
        "X-RapidAPI-Host: exercisedb.p.rapidapi.com",
        "X-RapidAPI-Key: 1172679aafmsh1f64d6691a8ee82p1fbc44jsna98d4b68e3a1"
    )
    @GET("exercises")
    suspend fun getAllExercises(): List<Exercise>

}