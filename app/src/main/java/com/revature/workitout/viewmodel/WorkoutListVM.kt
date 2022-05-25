package com.revature.workitout.viewmodel

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.revature.workitout.model.data.Result

class WorkoutListVM:ViewModel() {

    //Exercise List
    var exerciseList = RepositoryManager.exerciseRepo.getAllExercises
    private val bodypartList = MutableLiveData<List<String>>(listOf())
    var routineID:Long?=null

    init {
        loadExercises()
    }

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

    fun loadExerciseByBodypart(sBodypart:String){

//        viewModelScope.launch(Dispatchers.IO) {
            exerciseList = RepositoryManager.exerciseRepo.getExercisesByBodypart(sBodypart)
//        }
    }

    fun loadExercises(){

        viewModelScope.launch(Dispatchers.IO) {

            if(exerciseList.value != null){
                if(exerciseList.value!!.isEmpty()) {
                    Log.d("WorkoutlistVM", "Exercise Room Empty")
                    loadExercisesByAPI()
                }
            }
            bLoading.value = false
            loadBodyparts()
        }
    }

    private suspend fun loadExercisesByAPI() {

        val exerciseService = RetrofitHelper.getWorkoutService()
        val exerciseRepo = AllExercisesRepo(exerciseService)


        bLoading.value = when (val response = exerciseRepo.fetchAllExercises()) {

            is Result.Success -> {
                response.data.forEach { exercise ->
                    val temp = exercise.gifUrl.replace("http", "https")
                    exercise.gifUrl = temp
                }
                val list = mutableListOf<ExerciseEntity>()
                response.data.forEach {
                    val exe = ExerciseEntity(
                        id = it.id.toLong(),
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