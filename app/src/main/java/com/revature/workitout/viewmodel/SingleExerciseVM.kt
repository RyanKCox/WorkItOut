package com.revature.workitout.viewmodel

import androidx.compose.runtime.mutableStateOf
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

    var id = ""
    val exercise = MutableLiveData<Exercise>()
    val bLoading = mutableStateOf(true)

    fun loadExercise(){
        if(id != "") {
            viewModelScope.launch(Dispatchers.IO) {
                bLoading.value = when (singleExerciseRepo.fetchExerciseById(id)){
                    is ExerciseRepo.Result.Success->{

                        false
                    }
                    is ExerciseRepo.Result.Failure->{

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