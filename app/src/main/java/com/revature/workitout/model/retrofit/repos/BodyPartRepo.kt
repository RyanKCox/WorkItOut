package com.revature.workitout.model.retrofit

import android.util.Log

class BodypartRepo(private val exerciseService: WorkoutApiService) {

    sealed class Result{
        object Loading:Result()
        data class Success(val bodypartList:List<String>):Result()
        data class Failure(val throwable: Throwable):Result()
    }

    suspend fun fetchAllBodyparts():Result{
        return try{
            val result =
                exerciseService.getAllBodyParts()
            Result.Success(result)

        }catch (e:Exception){
            Log.d("BodypartRepo","Failed: ${e.message}")
            Result.Failure(e)
        }
    }

}