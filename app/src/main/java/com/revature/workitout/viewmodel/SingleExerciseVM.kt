package com.revature.workitout.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.RepositoryManager
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.entity.RoutineComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleExerciseVM: ViewModel() {

    //Display Screen
    var exercise by mutableStateOf(
        ExerciseEntity()
    )

    var bLoading by mutableStateOf(true)
    var bLoadingFailed by mutableStateOf(false)

    //AddRoutineScreen
    var nSet by mutableStateOf(5)
    var nRep by mutableStateOf(5)

    var routineID: Long? = null


    fun loadExercise(id:Long) {


        viewModelScope.launch(Dispatchers.IO) {
            exercise = RepositoryManager.exerciseRepo.getExerciseById(id)
            bLoading = false
        }

    }
    fun numInputLimit(num:Int):Int{
        if(num > 99){
            return num % 100
        }
        return num
    }

    fun addExercise(){

        if( routineID != null) {

            viewModelScope.launch(Dispatchers.IO) {
                val component = RoutineComponent(
                    id = 0,
                    RoutineID = routineID!!,
                    Rep = nRep,
                    Set = nSet,
                    sName = exercise.sName,
                    sTarget = exercise.sTarget,
                    sEquipment = exercise.sEquipment,
                    sBodypart = exercise.sBodypart,
                    sGifUrl = exercise.sGifUrl
                )
                RepositoryManager.routineRepo.addExerciseToRoutine(component)
            }
        }
    }



}