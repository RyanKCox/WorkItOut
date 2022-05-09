package com.revature.workitout.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.retrofit.RetrofitHelper
import com.revature.workitout.model.retrofit.allexcercises.AllExercisesRepo
import com.revature.workitout.model.retrofit.allexcercises.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutListVM:ViewModel() {

    //Retrofit Variables
    private val exerciseService = RetrofitHelper.getWorkoutService()
    private var exerciseRepo:AllExercisesRepo = AllExercisesRepo(exerciseService)


    //Excercise List
    private val exerciseList = MutableLiveData<List<Exercise>>(listOf())
    fun getExerciseList():LiveData<List<Exercise>>{
        return exerciseList
    }

    var bLoading = mutableStateOf(true)

    init {

        viewModelScope.launch(Dispatchers.IO) {
            loadExercises()
        }
    }

    private suspend fun loadExercises(){

        bLoading.value = when ( val response = exerciseRepo.fetchAllExercises()){

            is AllExercisesRepo.Result.Success->{
                response.exerciseList.forEach { exercise->
                    val temp = exercise.gifUrl.replace("http", "https")
                    exercise.gifUrl = temp
                }
                exerciseList.postValue(response.exerciseList)
                false
            }
            is AllExercisesRepo.Result.Failure->{
                Log.d("WorkoutVM","Loading Failed")
                false
            }
            is AllExercisesRepo.Result.Loading->{
                true
            }
        }

    }


}