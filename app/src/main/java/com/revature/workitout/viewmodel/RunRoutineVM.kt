package com.revature.workitout.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.revature.workitout.model.data.Routine
import com.revature.workitout.model.room.entity.RoutineComponent
import com.revature.workitout.model.room.repo.IRoutineRepo
import com.revature.workitout.model.room.repo.RoutineRepo
import com.revature.workitout.viewmodel.providers.CoroutineProvider
import kotlinx.coroutines.launch

class RunRoutineVM(
    private val routineRepo: IRoutineRepo,
    private val coroutineProvider: CoroutineProvider
):ViewModel() {
    var activeRoutine:MutableState <Routine?> = mutableStateOf(null)
    var activeExercise:MutableState<RoutineComponent?> = mutableStateOf(null)


    fun loadWorkout(routineID:Long){
        viewModelScope.launch(coroutineProvider.io){
            activeRoutine.value = routineRepo.getRoutineById(routineID)
            activeExercise.value = activeRoutine.value!!.exercises.first()
        }

    }

}

class RunRoutineVMFactory(private val routineRepo: RoutineRepo)
    :ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RunRoutineVM::class.java)){
            return RunRoutineVM(
                routineRepo = routineRepo,
                coroutineProvider = CoroutineProvider()
            ) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }

}