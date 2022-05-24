package com.revature.workitout.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.RepositoryManager
import com.revature.workitout.model.constants.RoutineBuilder
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.repo.ExerciseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleExerciseVM: ViewModel() {

    //Display Screen
    val exercise = MutableLiveData<ExerciseEntity>()
    val bLoading = mutableStateOf(true)
    val bLoadingFailed = mutableStateOf(false)

    //AddRoutineScreen
    var nSet = mutableStateOf(5)
    var nRep = mutableStateOf(5)

    var routineID: Long? = null

    var setList = RoutineBuilder.EXERCISE_SET_LIST
    var repList = RoutineBuilder.EXERCISE_REP_LIST

    fun loadExercise(id:Long,context: Context) {


        viewModelScope.launch(Dispatchers.IO) {
        val exerciseRoomRepo =
           ExerciseRepo(context.applicationContext as Application)

            val exe = exerciseRoomRepo.getExerciseById(id)

            if(exe.sBodypart == RoutineBuilder.EXERCISE_TYPE_CARDIO)
                setList = RoutineBuilder.CARDIO_SET_LIST

            exercise.postValue(exe)
            bLoading.value = false
        }

    }
    fun numInputLimit(num:Int):Int{
        if(num > 99){
            return num % 100
        }
        return num
    }

    fun addExercise(){

        if(exercise.value != null && routineID != null) {

            viewModelScope.launch(Dispatchers.IO) {
                val component = RoutineComponent(
                    id = 0,
                    RoutineID = routineID!!,
                    Rep = nRep.value,
                    Set = nSet.value,
                    sName = exercise.value!!.sName,
                    sTarget = exercise.value!!.sTarget,
                    sEquipment = exercise.value!!.sEquipment,
                    sBodypart = exercise.value!!.sBodypart,
                    sGifUrl = exercise.value!!.sGifUrl
                )
                RepositoryManager.routineRepo.addExerciseToRoutine(component)
            }
        }
    }



}