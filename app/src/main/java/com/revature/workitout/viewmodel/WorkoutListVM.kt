package com.revature.workitout.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.retrofit.BodypartRepo
import com.revature.workitout.model.retrofit.RetrofitHelper
import com.revature.workitout.model.retrofit.repos.AllExercisesRepo
import com.revature.workitout.model.retrofit.responses.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutListVM:ViewModel() {

    //Retrofit Variables
    private val exerciseService = RetrofitHelper.getWorkoutService()
    private var exerciseRepo: AllExercisesRepo = AllExercisesRepo(exerciseService)
    private var bodypartRepo:BodypartRepo = BodypartRepo(exerciseService)


    //Exercise List
    val exerciseList = MutableLiveData<List<Exercise>>(listOf())
    private val bodypartList = MutableLiveData<List<String>>(listOf())
    fun getBodyparts():LiveData<List<String>>{
        return bodypartList
    }

    //Display variables
    var bLoading = mutableStateOf(true)
    var bLoadingFailed = mutableStateOf(false)

    init {

        loadExercises()
        loadBodyparts()
    }
    private fun loadBodyparts(){
        viewModelScope.launch(Dispatchers.IO) {

            when(val response = bodypartRepo.fetchAllBodyparts()){
                is BodypartRepo.Result.Success ->{
                    var list = mutableListOf<String>()
                    list.add("All")
                    list.addAll(response.bodypartList)
                    bodypartList.postValue(list)
                }
                is BodypartRepo.Result.Failure->{
                    Log.d("WorkoutVM","Loading Bodyparts failed")
                }
                is BodypartRepo.Result.Loading->{

                }
            }
        }
    }

    fun LoadBodyPartbyPart(part:String){
        viewModelScope.launch(Dispatchers.IO){
            when(val response = exerciseRepo.fetchByBodypart(part)) {
                is AllExercisesRepo.Result.Success->{
                    response.exerciseList.forEach { exercise ->
                        val temp = exercise.gifUrl.replace("http", "https")
                        exercise.gifUrl = temp
                    }
                    exerciseList.postValue(response.exerciseList)
                }
                is AllExercisesRepo.Result.Failure->{
                    Log.d("WorkoutVM", "Loading Exercises Failed")
                }
                is AllExercisesRepo.Result.Loading->{}
            }
        }
    }
    fun loadExercises() {
        viewModelScope.launch(Dispatchers.IO) {

            bLoading.value = when (val response = exerciseRepo.fetchAllExercises()) {

                is AllExercisesRepo.Result.Success -> {
                    response.exerciseList.forEach { exercise ->
                        val temp = exercise.gifUrl.replace("http", "https")
                        exercise.gifUrl = temp
                    }
                    exerciseList.postValue(response.exerciseList)
                    false
                }
                is AllExercisesRepo.Result.Failure -> {
                    Log.d("WorkoutVM", "Loading Exercises Failed")
                    bLoadingFailed.value = true
                    false
                }
                is AllExercisesRepo.Result.Loading -> {
                    true
                }
            }

        }
    }


}