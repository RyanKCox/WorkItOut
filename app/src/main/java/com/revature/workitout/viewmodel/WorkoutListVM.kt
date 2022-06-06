package com.revature.workitout.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.RoutineComponent

class WorkoutListVM:ViewModel() {

    //Exercise List
    var exerciseList = mutableStateListOf<ExerciseEntity>()
    private var bodypartList = mutableStateListOf<String>()
    var routineID:Long?=null
    var selectedExercise:ExerciseEntity? = null
    var bDisplayExercise by mutableStateOf(false)

    init {
        loadExercises()
    }

    fun getBodyparts():SnapshotStateList<String>{
        return bodypartList
    }

    //Display variables
    var bLoading = mutableStateOf(true)
    var bLoadingFailed = mutableStateOf(false)

    private fun loadBodyparts(){

        viewModelScope.launch(Dispatchers.IO) {
            bodypartList.clear()
            bodypartList.add("All")
            bodypartList.addAll(RepositoryManager.exerciseRepo.getAllBodyParts())
        }
    }

    fun loadExerciseByBodypart(sBodypart:String){

        viewModelScope.launch(Dispatchers.IO) {
            exerciseList.clear()
            exerciseList.addAll(RepositoryManager.exerciseRepo.getExercisesByBodypart(sBodypart))
        }
    }

    fun loadExercises(){

        viewModelScope.launch(Dispatchers.IO) {

            exerciseList.clear()
            exerciseList.addAll(RepositoryManager.exerciseRepo.getAllExercises())
            if(exerciseList.isEmpty()){
                Log.d("WorkoutlistVM", "Exercise Room Empty")
                loadExercisesByAPI()

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
                exerciseList.addAll( RepositoryManager.exerciseRepo.getAllExercises())
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


    fun addExerciseToRoutine(nSet:Int,nRep:Int, routineVM: RoutineVM){

        if( routineID != null && selectedExercise != null) {

            viewModelScope.launch(Dispatchers.IO) {
                val component = RoutineComponent(
                    id = 0,
                    RoutineID = routineID!!,
                    Rep = nRep,
                    Set = nSet,
                    sName = selectedExercise!!.sName,
                    sTarget = selectedExercise!!.sTarget,
                    sEquipment = selectedExercise!!.sEquipment,
                    sBodypart = selectedExercise!!.sBodypart,
                    sGifUrl = selectedExercise!!.sGifUrl
                )
                RepositoryManager.routineRepo.addExerciseToRoutine(component)
                routineVM.loadRoutines()
                routineVM.reloadSelectedRoutine()
            }
        }
    }
}