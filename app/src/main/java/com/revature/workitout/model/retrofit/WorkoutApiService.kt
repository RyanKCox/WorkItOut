package com.revature.workitout.model.retrofit

import com.revature.workitout.model.retrofit.responses.Exercise
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkoutApiService {

    @GET("exercises")
    suspend fun getAllExercises(): List<Exercise>

    @GET("exercises/bodyPartList")
    suspend fun getAllBodyParts():List<String>

    @GET("exercises/bodyPart/{part}")
    suspend fun getByBodyPart(@Path("part")part:String):List<Exercise>

    @GET("exercises/exercise/{id}")
    suspend fun getExerciseById(@Path("id") id:String):Exercise

}