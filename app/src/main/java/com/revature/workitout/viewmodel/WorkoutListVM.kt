package com.revature.workitout.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.RepositoryManager
import com.revature.workitout.model.retrofit.RetrofitHelper
import com.revature.workitout.model.retrofit.repos.AllExercisesRepo
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.repo.ExerciseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.revature.workitout.model.data.Result
import com.revature.workitout.model.retrofit.repos.ExerciseAPIRepo

class WorkoutListVM:ViewModel() {

    //Exercise List
    var exerciseList = RepositoryManager.exerciseRepo.getAllExercises
    private val bodypartList = MutableLiveData<List<String>>(listOf())
    fun getBodyparts():LiveData<List<String>>{
        return bodypartList
    }

    //Display variables
    var bLoading = mutableStateOf(true)
    var bLoadingFailed = mutableStateOf(false)

    private fun loadBodyparts(){

        viewModelScope.launch(Dispatchers.IO) {
            bodypartList.postValue(RepositoryManager.exerciseRepo.getAllBodyParts())
        }
    }

    fun loadExerciseByBodypart(sBodypart:String,context: Context){

        viewModelScope.launch(Dispatchers.IO) {
        val exerciseRoomRepo = ExerciseRepo(context.applicationContext as Application)

            exerciseList = exerciseRoomRepo.getExercisesByBodypart(sBodypart)
        }
    }

    fun loadExercises(context: Context){

        viewModelScope.launch(Dispatchers.IO) {

//            val exerciseRoomRepo = ExerciseRepo(context.applicationContext as Application)
//
//            val list = exerciseRoomRepo.getAllExercises

            if(exerciseList.value.isNullOrEmpty()){
                Log.d("WorkoutlistVM", "Exercise Room Empty")
                loadExercisesByAPI(context)

            }
//            if(list.isNotEmpty()) {
//                Log.d("WorkoutlistVM", "Exercise Room Not Empty")
//                exerciseList = RepositoryManager.exerciseRepo.getAllExercises
////                exerciseList.postValue(exerciseRoomRepo.getAllExercises)
//                bLoading.value = false
//            } else{
//                Log.d("WorkoutlistVM", "Exercise Room Empty")
//                loadExercisesByAPI(context)
//            }

            loadBodyparts()
        }
    }

    private fun loadExercisesByAPI(context: Context) {

        val exerciseService = RetrofitHelper.getWorkoutService()
        val exerciseRepo = AllExercisesRepo(exerciseService)

        viewModelScope.launch(Dispatchers.IO) {

            bLoading.value = when (val response = exerciseRepo.fetchAllExercises()) {

                is Result.Success -> {
                    response.data.forEach { exercise ->
                        val temp = exercise.gifUrl.replace("http", "https")
                        exercise.gifUrl = temp
                    }
                    val list = mutableListOf<ExerciseEntity>()
                    response.data.forEach {
                        val exe = ExerciseEntity(
                            id = it.id.toInt(),
                            sName = it.name,
                            sTarget = it.target,
                            sBodypart = it.bodyPart,
                            sEquipment = it.equipment,
                            sGifUrl = it.gifUrl
                        )
                        RepositoryManager.exerciseRepo.insertExercise(exe)
                        list.add(exe)
                    }
                    exerciseList = RepositoryManager.exerciseRepo.getAllExercises
                    false
                }
                is Result.Error -> {
                    Log.d("WorkoutVM", "Loading Exercises Failed\nError - ${response.exception}")
                    bLoadingFailed.value = true
                    false
                }
                is Result.Loading -> {
                    true
                }
            }
        }
    }
}