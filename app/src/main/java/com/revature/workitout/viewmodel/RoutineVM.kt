package com.revature.workitout.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.RepositoryManager
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.entity.RoutineEntity
import com.revature.workitout.model.room.repo.RoutineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineVM:ViewModel() {

    var routineList :List<Routine>? = null
    var selectedRoutine: MutableState<Routine?> = mutableStateOf(null)
    var exerciseList = listOf<ExerciseEntity>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadRoutines(true)
        }
    }

    fun deleteRoutine(){
        if (selectedRoutine.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                RepositoryManager.routineRepo.deleteRoutine(selectedRoutine.value!!.routineEntity)
                loadRoutines(true)
            }
        }
    }
    fun createRoutine(){
        viewModelScope.launch(Dispatchers.IO){
            var sName = "Test"
            sName += routineList!!.size.toString()
            val id = RepositoryManager.routineRepo.addRoutine(RoutineEntity(
                id = 0,
                name = sName
            ))
            loadRoutines()
            selectedRoutine.value = if(routineList!!.isEmpty()) null else routineList!!.last()

        }
//        loadExercises(context)
    }
//    fun loadExercises(context:Context){
//
//        exerciseList = listOf()
//
//        if(selectedRoutine.value != null){
//            viewModelScope.launch(Dispatchers.IO) {
//                val componentList =
//                    RepositoryManager.routineRepo.getExercisesOfRoutine(
//                        selectedRoutine.value!!.id
//                    )
//                if(componentList.isNotEmpty()){
//                    val exeList = mutableListOf<ExerciseEntity>()
//                    componentList.forEach {
//                        exeList.add(RepositoryManager.exerciseRepo.getExerciseById(it.ExerciseID))
//                    }
//                    exerciseList = exeList
//                }
//            }
//        }
//    }

    private suspend fun loadRoutines(bSetSelected:Boolean = false){
            routineList = RepositoryManager.routineRepo.getAllRoutinesWithExercises()
            if(bSetSelected)
                selectedRoutine.value = if(routineList!!.isEmpty()) null else routineList!!.first()
    }
}