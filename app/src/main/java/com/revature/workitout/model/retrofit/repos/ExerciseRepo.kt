package com.revature.workitout.model.retrofit.repos

import android.util.Log
import com.revature.workitout.model.retrofit.WorkoutApiService
import com.revature.workitout.model.retrofit.responses.ExerciseResponse

class ExerciseAPIRepo(private val exerciseService: WorkoutApiService) {

    sealed class Result{
        object Loading:Result()
        data class Success(val exerciseResponse:ExerciseResponse):Result()
        data class Failure(val throwable: Throwable):Result()
    }

    suspend fun fetchExerciseById(id:String):Result{
        return try{
            val result =
                exerciseService.getExerciseById(id)
            Result.Success(result)

        }catch (e:Exception){
            Log.d("ExerciseRepo","Failed: ${e.message}")
            Result.Failure(e)
        }
    }
}