package com.revature.workitout.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.entity.RoutineEntity
import com.revature.workitout.model.room.repo.IRoutineRepo
import com.revature.workitout.model.room.repo.RoutineRepo
import com.revature.workitout.viewmodel.providers.CoroutineProvider
import kotlinx.coroutines.launch

class RoutineVM(
    private val routineRepo: IRoutineRepo,
    private val coroutineProvider: CoroutineProvider
    ):ViewModel() {

    var routineList :List<Routine>? = null

    var selectedRoutine: MutableState<Routine?> = mutableStateOf(null)
    var selectedComponent: MutableState<RoutineComponent?> = mutableStateOf(null)


    var addRoutineDialog by mutableStateOf(false)
    var bDisplayExercise by mutableStateOf(false)


    init {
        viewModelScope.launch(coroutineProvider.io) {
            loadRoutines(true)
        }
    }

    fun deleteRoutine(){
        if (selectedRoutine.value != null) {
            viewModelScope.launch(coroutineProvider.io) {
                routineRepo.deleteRoutine(selectedRoutine.value!!.routineEntity)
                selectedRoutine.value = routineList?.firstOrNull()
                loadRoutines(/*true*/routineList!!.isNotEmpty())
            }
        }
    }
    fun createRoutine(sName:String){
        viewModelScope.launch(coroutineProvider.io){
            routineRepo.addRoutine(RoutineEntity(
                id = 0,
                name = sName
            ))
            loadRoutines()
            selectedRoutine.value = if(routineList!!.isEmpty()) null else routineList!!.last()

        }
    }

    suspend fun loadRoutines(bSetSelected:Boolean = false){
            routineList = routineRepo.getAllRoutinesWithExercises()
            if(bSetSelected)
                selectedRoutine.value = if(routineList!!.isEmpty()) null else routineList!!.first()
    }
    suspend fun reloadSelectedRoutine(){
        if(selectedRoutine.value != null){
            selectedRoutine.value = routineRepo.getRoutineById(selectedRoutine.value!!.routineEntity.id)
        }
    }

    fun updateComponent(){
        if(selectedComponent.value != null){
            viewModelScope.launch(coroutineProvider.io){
                routineRepo.insertComponent(selectedComponent.value!!)
            }
        }
    }

}
class RoutineVMFactory(private val routineRepo: RoutineRepo):
        ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RoutineVM::class.java)){
            return RoutineVM(routineRepo,CoroutineProvider()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}