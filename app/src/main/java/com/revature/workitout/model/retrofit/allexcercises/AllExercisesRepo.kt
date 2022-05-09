package com.revature.workitout.model.retrofit.allexcercises

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revature.workitout.model.retrofit.WorkoutApiService
import java.lang.Exception

class AllExercisesRepo(val exerciseService: WorkoutApiService) {

    sealed class Result{
        object Loading:Result()
        data class Success(val exerciseList: List<Exercise>):Result()
        data class Failure(val throwable: Throwable):Result()
    }

    suspend fun fetchAllExercises():Result{
        return try{
            val result =
                exerciseService.getAllExercises()

            Result.Success(result)

        } catch (e:Exception){
            Log.d("ExerciseRepo", "Failed: ${e.message}")
            Result.Failure(e)
        }
    }
}