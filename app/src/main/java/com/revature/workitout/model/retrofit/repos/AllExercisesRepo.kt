package com.revature.workitout.model.retrofit.repos

import android.util.Log
import com.revature.workitout.model.retrofit.WorkoutApiService
import com.revature.workitout.model.retrofit.responses.Exercise
import java.lang.Exception

class AllExercisesRepo(private val exerciseService: WorkoutApiService) {

    sealed class Result{
        object Loading: Result()
        data class Success(val exerciseList: List<Exercise>): Result()
        data class Failure(val throwable: Throwable): Result()
    }

    suspend fun fetchAllExercises(): Result {
        return try{
            val result =
                exerciseService.getAllExercises()

            Result.Success(result)

        } catch (e:Exception){
            Log.d("AllExerciseRepo", "Failed: ${e.message}")
            Result.Failure(e)
        }
    }

    suspend fun fetchByBodypart(part:String): Result {
        return try{
            val result =
                exerciseService.getByBodyPart(part)
            Result.Success(result)
        } catch (e:Exception){
            Log.d("AllExerciseRepo","Failed: ${e.message}")
            Result.Failure(e)
        }
    }
}