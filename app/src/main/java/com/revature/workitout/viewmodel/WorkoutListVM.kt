package com.revature.workitout.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.retrofit.RetrofitHelper
import com.revature.workitout.model.retrofit.repos.AllExercisesRepo
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.repo.ExerciseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutListVM:ViewModel() {

    //Exercise List
    var exerciseList = MutableLiveData<List<ExerciseEntity>>(listOf())
    private val bodypartList = MutableLiveData<List<String>>(listOf())
    fun getBodyparts():LiveData<List<String>>{
        return bodypartList
    }

    //Display variables
    var bLoading = mutableStateOf(true)
    var bLoadingFailed = mutableStateOf(false)

    private fun loadBodyparts(exerciseRoomRepo: ExerciseRepo){

        viewModelScope.launch(Dispatchers.IO) {
            bodypartList.postValue(exerciseRoomRepo.getAllBodyParts())
        }
    }

    fun loadExerciseByBodypart(sBodypart:String,context: Context){

        viewModelScope.launch(Dispatchers.IO) {
        val exerciseRoomRepo = ExerciseRepo(context.applicationContext as Application)

            exerciseList.postValue(exerciseRoomRepo.getExercisesByBodypart(sBodypart))
        }
    }

    fun loadExercises(context: Context){

        viewModelScope.launch(Dispatchers.IO) {

            val exerciseRoomRepo = ExerciseRepo(context.applicationContext as Application)

            val list = exerciseRoomRepo.getAllExercises

            if(list.isNotEmpty()) {
                Log.d("WorkoutlistVM", "Exercise Room Not Empty")
                exerciseList.postValue(exerciseRoomRepo.getAllExercises)
                bLoading.value = false
            } else{
                Log.d("WorkoutlistVM", "Exercise Room Empty")
                loadExercisesByAPI(exerciseRoomRepo)
            }

            loadBodyparts(exerciseRoomRepo)
        }
    }

    private fun loadExercisesByAPI(exerciseRoomRepo:ExerciseRepo) {

        val exerciseService = RetrofitHelper.getWorkoutService()
        val exerciseRepo = AllExercisesRepo(exerciseService)

        viewModelScope.launch(Dispatchers.IO) {

            bLoading.value = when (val response = exerciseRepo.fetchAllExercises()) {

                is AllExercisesRepo.Result.Success -> {
                    response.exerciseList.forEach { exercise ->
                        val temp = exercise.gifUrl.replace("http", "https")
                        exercise.gifUrl = temp
                    }
                    val list = mutableListOf<ExerciseEntity>()
                    response.exerciseList.forEach {
                        val exe = ExerciseEntity(
                            id = it.id.toInt(),
                            sName = it.name,
                            sTarget = it.target,
                            sBodypart = it.bodyPart,
                            sEquipment = it.equipment,
                            sGifUrl = it.gifUrl
                        )
                        exerciseRoomRepo.insertExercise(exe)
                        list.add(exe)
                    }
                    exerciseList.postValue(list)
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