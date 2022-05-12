package com.revature.workitout.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.retrofit.RetrofitHelper
import com.revature.workitout.model.retrofit.repos.ExerciseRepo
import com.revature.workitout.model.retrofit.responses.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleExerciseVM: ViewModel() {

    //Retrofit Variables
    private val exerciseService = RetrofitHelper.getWorkoutService()
    private val singleExerciseRepo = ExerciseRepo(exerciseService)

    //Display Screen
    var exerciseId = ""
    val exercise = MutableLiveData<Exercise>()
    val bLoading = mutableStateOf(true)
    val bLoadingFailed = mutableStateOf(false)

    //AddRoutineScreen
    var sSet = mutableStateOf("5")
    var sRep = mutableStateOf("5")

    fun loadExercise(id:String){
        if(id != "") {
            exerciseId=id
            viewModelScope.launch(Dispatchers.IO) {
                bLoading.value = when (val result = singleExerciseRepo.fetchExerciseById(exerciseId)){
                    is ExerciseRepo.Result.Success->{
                        result.exercise.gifUrl = result.exercise.gifUrl.replace("http","https")

                        result.exercise.name = makeUppercase(result.exercise.name)

                        exercise.postValue(result.exercise)
                        false
                    }
                    is ExerciseRepo.Result.Failure->{
                        bLoadingFailed.value = true
                        Log.d("SingleVM","Loading Exercise failed")
                        false
                    }
                    is ExerciseRepo.Result.Loading->{
                        true
                    }
                }
            }
        }
    }


}