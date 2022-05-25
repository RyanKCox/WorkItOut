package com.revature.workitout.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revature.workitout.RepositoryManager
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.ExerciseEntity
import com.revature.workitout.model.room.entity.RoutineEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineVM:ViewModel() {

    var routineList :List<Routine>? = null
    var selectedRoutine: MutableState<Routine?> = mutableStateOf(null)
    var addRoutineDialog by mutableStateOf(false)

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
    fun createRoutine(sName:String){
        viewModelScope.launch(Dispatchers.IO){
//            var sName = "Test"
//            sName += routineList!!.size.toString()
            RepositoryManager.routineRepo.addRoutine(RoutineEntity(
                id = 0,
                name = sName
            ))
            loadRoutines()
            selectedRoutine.value = if(routineList!!.isEmpty()) null else routineList!!.last()

        }
    }

    private suspend fun loadRoutines(bSetSelected:Boolean = false){
            routineList = RepositoryManager.routineRepo.getAllRoutinesWithExercises()
            if(bSetSelected)
                selectedRoutine.value = if(routineList!!.isEmpty()) null else routineList!!.first()
    }
}