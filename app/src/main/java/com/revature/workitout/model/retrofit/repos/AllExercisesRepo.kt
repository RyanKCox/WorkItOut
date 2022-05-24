package com.revature.workitout.model.retrofit.repos

import android.util.Log
import com.revature.workitout.model.retrofit.WorkoutApiService
import com.revature.workitout.model.retrofit.responses.ExerciseResponse
import java.lang.Exception
import com.revature.workitout.model.data.Result

class AllExercisesRepo(private val exerciseService: WorkoutApiService) {

//    sealed class Result{
//        object Loading: Result()
//        data class Success(val exerciseList: List<Exercise>): Result()
//        data class Failure(val throwable: Throwable): Result()
//    }

    suspend fun fetchAllExercises(): Result<List<ExerciseResponse>> {
        return try{
            val result =
                exerciseService.getAllExercises()

            Result.Success<List<ExerciseResponse>>(result)

        } catch (e:Exception){
            Log.d("AllExerciseRepo", "Failed: ${e.message}")
            Result.Error(e)
        }
    }

    suspend fun fetchByBodypart(part:String): Result<List<ExerciseResponse>> {
        return try{
            val result =
                exerciseService.getByBodyPart(part)
            Result.Success(result)
        } catch (e:Exception){
            Log.d("AllExerciseRepo","Failed: ${e.message}")
            Result.Error(e)
        }
    }
}